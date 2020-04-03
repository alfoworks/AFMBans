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
import ru.allformine.afmbans.net.api.ban.error.ApiException;

import java.util.Optional;

public class CommandUnmute extends Command {
    @Override
    public CommandResult execute(CommandSource src, CommandContext args) throws CommandException {
        Optional<String> nickname = args.getOne("player");

        if (!nickname.isPresent()) {
            throw new CommandException(getReplyText(PluginMessages.NOT_ENOUGH_ARGUMENTS, TextType.ERROR));
        }

        BanAPI banApi = new BanAPI(nickname.get());
        boolean success;

        try {
            success = banApi.amnesty(src, PunishType.MUTE).get("ok").getAsBoolean();
        } catch (ApiException error) {
            if (error.getErrorCode() == 101) {
                throw new CommandException(getReplyText(PluginMessages.PLAYER_NOT_MUTED, TextType.ERROR));
            }

            throw new CommandException(getReplyText(PluginMessages.API_ERROR, TextType.ERROR));
        } catch (Exception e) {
            e.printStackTrace();
            throw new CommandException(getReplyText(PluginMessages.UNKNOWN_ERROR, TextType.ERROR));
        }

        if (success) {
            src.sendMessage(getReplyText(PluginMessages.UNMUTE_SUCCESSFUL, TextType.OK));
            PluginUtils.getBroadcastPunishMessage(src, nickname.get(), ActionType.UNMUTE, null, null, false);
        } else {
            src.sendMessage(getReplyText(PluginMessages.PLAYER_NOT_MUTED, TextType.OK));
        }

        return CommandResult.success();
    }
}
