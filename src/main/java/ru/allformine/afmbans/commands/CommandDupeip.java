package ru.allformine.afmbans.commands;

import org.spongepowered.api.command.CommandException;
import org.spongepowered.api.command.CommandResult;
import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.command.args.CommandContext;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.text.format.TextColor;
import org.spongepowered.api.text.format.TextColors;
import ru.allformine.afmbans.PluginMessages;
import ru.allformine.afmbans.net.api.ban.BanAPI;
import ru.allformine.afmbans.net.api.ban.error.ApiError;
import ru.allformine.afmbans.net.api.ban.response.IpHistoryResponse;
import ru.allformine.afmbans.net.api.ban.response.object.IpHistoryRecord;

import java.io.IOException;
import java.net.InetAddress;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public class CommandDupeip extends Command {

    private IpHistoryRecord getIpFromHistory(String nickname) throws IOException, ApiError {
        IpHistoryResponse response = BanAPI.getIpHistory(nickname, null);
        return response.items.get(response.items.size() - 1);
    }

    @Override
    public CommandResult execute(CommandSource src, CommandContext args) throws CommandException {
        Optional<String> protoNick = args.getOne("player");
        if(protoNick.isPresent()){
            String nick = protoNick.get();
            IpHistoryResponse ipHistoryResponse;
            try {
                InetAddress address = getIpFromHistory(nick).ip;
                ipHistoryResponse = BanAPI.getIpHistory(null, address);
            } catch (Exception e) { // ApiError, IOE
                e.printStackTrace();
                throw new CommandException(getReplyText(PluginMessages.UNKNOWN_ERROR, TextType.ERROR));
            }
            List<IpHistoryRecord> items = ipHistoryResponse.items;
            List<String> nicknames = new ArrayList<>();
            for(IpHistoryRecord record: items) nicknames.add(record.nickname);
            Map<String, Boolean> response;
            try {
                response = BanAPI.massBanCheck(nicknames);
            } catch (Exception e) { // ApiError, IOE
                e.printStackTrace();
                throw new CommandException(getReplyText(PluginMessages.UNKNOWN_ERROR, TextType.ERROR));
            }
            Text.Builder builder = Text.builder();
            builder.append(Text.of("Известные аккаунты игрока " + nick + ":\n"));
            response.forEach((nickname, punished) -> {
                TextColor color = TextColors.GREEN;
                if(punished){
                    color = TextColors.RED;
                }
                builder.append(colorText(nickname, color));
                builder.append(Text.of(", "));
            });
            src.sendMessage(builder.build());
        }else{
            src.sendMessage(getReplyText("Недостаточно аргументов!", TextType.ERROR));
        }

        return CommandResult.success();
    }
}
