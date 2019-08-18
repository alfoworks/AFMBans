package ru.allformine.afmbans.commands;

import org.spongepowered.api.command.CommandException;
import org.spongepowered.api.command.CommandResult;
import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.command.args.CommandContext;
import org.spongepowered.api.text.Text;
import ru.allformine.afmbans.net.api.ban.BanAPI;
import ru.allformine.afmbans.net.api.ban.response.IpHistoryResponse;
import ru.allformine.afmbans.net.api.ban.response.object.IpHistoryRecord;

import java.util.List;
import java.util.Optional;

public class CommandDupeip extends Command {
    @Override
    public CommandResult execute(CommandSource src, CommandContext args) throws CommandException {
        Optional<String> protoNick= args.<String>getOne("player");
        if(protoNick.isPresent()){
            String nick = protoNick.get();
            IpHistoryResponse response;
            try {
                response = BanAPI.getIpHistory(nick, null);
            } catch (Exception e) { // ApiError, IOE
                e.printStackTrace();
                throw new CommandException(getReplyText("Произошла неизвестная ошибка.", TextType.ERROR));
            }
            List<IpHistoryRecord> items = response.items;
            Text.Builder builder = Text.builder();
            for(IpHistoryRecord record: items){

            }
            src.sendMessage(getReplyText(builder.toString(), TextType.OK));
        }else{
            src.sendMessage(getReplyText("Недостаточно аргументов!", TextType.ERROR));
        }

        // PluginStatics.DEBUG_MODE = !PluginStatics.DEBUG_MODE;

        // src.sendMessage(getReplyText("Режим отладки был: " + (PluginStatics.DEBUG_MODE ? "включен" : "выключен"), TextType.OK));

        return CommandResult.success();
    }
}