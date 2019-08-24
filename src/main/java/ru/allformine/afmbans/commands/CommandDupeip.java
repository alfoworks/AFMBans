package ru.allformine.afmbans.commands;

import org.spongepowered.api.command.CommandException;
import org.spongepowered.api.command.CommandResult;
import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.command.args.CommandContext;
import org.spongepowered.api.text.Text;
import ru.allformine.afmbans.net.api.ban.BanAPI;
import ru.allformine.afmbans.net.api.ban.error.ApiError;
import ru.allformine.afmbans.net.api.ban.response.IpHistoryResponse;
import ru.allformine.afmbans.net.api.ban.response.object.IpHistoryRecord;

import java.io.IOException;
import java.net.InetAddress;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class CommandDupeip extends Command {

    private IpHistoryRecord getIpFromHistory(String nickname) throws IOException, ApiError {
        IpHistoryResponse response = BanAPI.getIpHistory(nickname, null);
        return response.items.get(response.items.size() - 1);
    }

    @Override
    public CommandResult execute(CommandSource src, CommandContext args) throws CommandException {
        Optional<String> protoNick = args.<String>getOne("player");
        if(protoNick.isPresent()){
            String nick = protoNick.get();
            IpHistoryResponse response;
            try {
                InetAddress address = getIpFromHistory(nick).ip;
                response = BanAPI.getIpHistory(null, address);
            } catch (Exception e) { // ApiError, IOE
                e.printStackTrace();
                throw new CommandException(getReplyText("Произошла неизвестная ошибка.", TextType.ERROR));
            }
            List<IpHistoryRecord> items = response.items;
            List<String> nicknames = new ArrayList<>();
            for(IpHistoryRecord record: items){
                String nickname = record.nickname;
                nicknames.add(nickname);
            }

            Text.Builder builder = Text.builder();
            src.sendMessage(builder.toText());
        }else{
            src.sendMessage(getReplyText("Недостаточно аргументов!", TextType.ERROR));
        }

        // PluginStatics.DEBUG_MODE = !PluginStatics.DEBUG_MODE;

        // src.sendMessage(getReplyText("Режим отладки был: " + (PluginStatics.DEBUG_MODE ? "включен" : "выключен"), TextType.OK));

        return CommandResult.success();
    }
}
