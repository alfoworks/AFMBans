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

public class CommandUnban extends Command {
    @Override
    public CommandResult execute(CommandSource src, CommandContext args) throws CommandException {
        String nick = args.<String>getOne("player").get();
        BanAPI banApi = new BanAPI(nick);

        boolean ok;

        try {
            ok = banApi.amnesty(src, PunishType.BAN).get("ok").getAsBoolean();
        } catch (Exception e) {
            throw new CommandException(getReplyText(PluginMessages.UNKNOWN_ERROR, TextType.ERROR));
        }

        if (!ok) throw new CommandException(getReplyText(PluginMessages.PLAYER_NOT_BANNED, TextType.ERROR));

        src.sendMessage(getReplyText(PluginMessages.UNBAN_SUCCESSFUL, TextType.OK));
        PluginUtils.broadcastPunishMessage(src, nick, ActionType.UNBAN);

        return CommandResult.success();
    }
}
