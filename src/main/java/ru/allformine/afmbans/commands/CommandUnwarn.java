package ru.allformine.afmbans.commands;

import org.spongepowered.api.command.CommandException;
import org.spongepowered.api.command.CommandResult;
import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.command.args.CommandContext;
import ru.allformine.afmbans.ActionType;
import ru.allformine.afmbans.PluginMessages;
import ru.allformine.afmbans.PluginStatics;
import ru.allformine.afmbans.PluginUtils;
import ru.allformine.afmbans.net.api.ban.BanAPI;
import ru.allformine.afmbans.net.api.ban.PunishType;
import ru.allformine.afmbans.net.api.ban.error.ApiException;

import java.util.Optional;

public class CommandUnwarn extends Command {
    @Override
    public CommandResult execute(CommandSource src, CommandContext args) throws CommandException {
        Optional<String> protoNick = args.getOne("player");
        if (!protoNick.isPresent())
            throw new CommandException(getReplyText(PluginMessages.NOT_ENOUGH_ARGUMENTS, TextType.ERROR));
        String nick = PluginUtils.getTrueNickCase(protoNick.get());

        BanAPI banApi = new BanAPI(nick);

        boolean ok;

        try {
            ok = banApi.amnesty(src, PunishType.WARN).get("ok").getAsBoolean();
        } catch (ApiException e) {
            if (e.getErrorCode() == 101) {
                throw new CommandException(getReplyText(PluginMessages.PLAYER_HAS_NO_WARNS, TextType.ERROR));
            }

            throw new CommandException(getReplyText(PluginMessages.API_ERROR, TextType.ERROR));
        } catch (Exception e) {
            throw new CommandException(getReplyText(PluginMessages.UNKNOWN_ERROR, TextType.ERROR));
        }

        if (!ok) throw new CommandException(getReplyText(PluginMessages.API_ERROR, TextType.ERROR));

        PluginStatics.broadcastChannel.send(PluginUtils.getBroadcastPunishMessage(src, nick, ActionType.UNWARN, null, null, false));

        src.sendMessage(getReplyText(PluginMessages.UNWARN_SUCCESSFUL, TextType.OK));

        return CommandResult.success();
    }
}
