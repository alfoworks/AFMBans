package ru.allformine.afmbans.commands;

import org.spongepowered.api.command.CommandException;
import org.spongepowered.api.command.CommandResult;
import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.command.args.CommandContext;
import ru.allformine.afmbans.ActionType;
import ru.allformine.afmbans.PluginMessages;
import ru.allformine.afmbans.PluginUtils;
import ru.allformine.afmbans.net.api.ban.BanAPI;
import ru.allformine.afmbans.net.api.ban.PunishType;
import ru.allformine.afmbans.net.api.ban.error.ApiError;

import java.util.Optional;

public class CommandUnban extends Command {
    @Override
    public CommandResult execute(CommandSource src, CommandContext args) throws CommandException {
        Optional<String> protoNick = args.<String>getOne("player");
        if(protoNick.isPresent()) {
            BanAPI banApi = new BanAPI(protoNick.get());
            try {
                banApi.amnesty(src, PunishType.BAN).get("ok").getAsBoolean();
            } catch (ApiError error) {
                if (error.getErrorCode() == 101) {
                    throw new CommandException(getReplyText(PluginMessages.PLAYER_NOT_BANNED, TextType.ERROR));
                }
                throw new CommandException(getReplyText(PluginMessages.API_ERROR, TextType.ERROR));
            } catch (Exception e) {
                e.printStackTrace();
                throw new CommandException(getReplyText(PluginMessages.UNKNOWN_ERROR, TextType.ERROR));
            }

            src.sendMessage(getReplyText(PluginMessages.UNBAN_SUCCESSFUL, TextType.OK));
            PluginUtils.getPunishMessage(src, protoNick.get(), ActionType.UNBAN);
        }else{
            throw new CommandException(getReplyText(PluginMessages.NOT_ENOUGH_ARGUMENTS, TextType.ERROR));
        }
        return CommandResult.success();
    }
}
