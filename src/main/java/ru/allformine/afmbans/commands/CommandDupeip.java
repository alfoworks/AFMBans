package ru.allformine.afmbans.commands;

import org.spongepowered.api.command.CommandException;
import org.spongepowered.api.command.CommandResult;
import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.command.args.CommandContext;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.text.format.TextColors;
import ru.allformine.afmbans.AFMBans;
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

    private Optional<IpHistoryRecord> getIpFromHistory(String nickname) throws IOException, ApiError {
        IpHistoryResponse response = BanAPI.getIpHistory(nickname, null);
        if(response.items.size() > 0)
            return Optional.of(response.items.get(response.items.size() - 1));
        else return Optional.empty();

    }

    @Override
    public CommandResult execute(CommandSource src, CommandContext args) throws CommandException {
        Optional<String> protoNick = args.getOne("player");
        if (!protoNick.isPresent())
            throw new CommandException(getReplyText(PluginMessages.NOT_ENOUGH_ARGUMENTS, TextType.ERROR));
        String nick = protoNick.get();
        IpHistoryResponse ipHistoryResponse;
        Map<String, Boolean> response;
        try {
            Optional<IpHistoryRecord> ip_record = getIpFromHistory(nick);
            if(!ip_record.isPresent())
                throw new CommandException(getReplyText(PluginMessages.PLAYER_UNKNOWN, TextType.ERROR));
            InetAddress address = ip_record.get().ip;
            ipHistoryResponse = BanAPI.getIpHistory(null, address);
            List<IpHistoryRecord> items = ipHistoryResponse.items;
            List<String> nicknames = new ArrayList<>();
            for(IpHistoryRecord record: items) nicknames.add(record.nickname);
            response = BanAPI.massBanCheck(nicknames);
        } catch (IOException e) {
            e.printStackTrace();
            throw new CommandException(getReplyText(PluginMessages.UNKNOWN_ERROR, TextType.ERROR));
        } catch (ApiError e){
            AFMBans.logger.error("[" + e.getErrorCode() + "]: " + e.getDescription());
            throw new CommandException(getReplyText(PluginMessages.API_ERROR, TextType.ERROR));
        }
        Text.Builder builder = Text.builder();
        builder.append(Text.of("Известные аккаунты игрока " + nick + ":\n"));
        List<Text> text = new ArrayList<>();
        response.forEach((nickname, punished) -> text.add(colorText(nickname, punished ? TextColors.RED : TextColors.GREEN)));
        builder.append(Text.joinWith(Text.of(", "), text));
        src.sendMessage(builder.build());

        return CommandResult.success();
    }
}
