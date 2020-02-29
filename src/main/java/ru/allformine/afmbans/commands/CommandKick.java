package ru.allformine.afmbans.commands;

import org.spongepowered.api.command.CommandException;
import org.spongepowered.api.command.CommandResult;
import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.command.args.CommandContext;
import org.spongepowered.api.entity.living.player.Player;
import ru.allformine.afmbans.ActionType;
import ru.allformine.afmbans.PluginMessages;
import ru.allformine.afmbans.PluginStatics;
import ru.allformine.afmbans.PluginUtils;
import ru.allformine.afmbans.net.api.ban.BanAPI;
import ru.allformine.afmbans.net.api.ban.PunishType;

public class CommandKick extends Command {
    @Override
    public CommandResult execute(CommandSource src, CommandContext args) throws CommandException {
        if (!args.<Player>getOne("player").isPresent()) {
            throw new CommandException(getReplyText(PluginMessages.NOT_ENOUGH_ARGUMENTS, TextType.ERROR));
        }

        Player player = args.<Player>getOne("player").get();
        String reason = args.<String>getOne("reason").orElse(PluginStatics.DEFAULT_REASON);

        BanAPI banApi = new BanAPI(player);
        boolean ok;

        try {
            ok = banApi.punish(src, PunishType.KICK, reason, null, null).get("ok").getAsBoolean();
        } catch (Exception e) {
            throw new CommandException(getReplyText(PluginMessages.UNKNOWN_ERROR, TextType.ERROR));
        }

        if (!ok) throw new CommandException(getReplyText(PluginMessages.API_ERROR, TextType.ERROR));

        player.kick(PluginUtils.getPunishMessageForPlayer(PunishType.KICK, src.getName(), reason, null));
        PluginStatics.broadcastChannel.send(PluginUtils.getBroadcastPunishMessage(src, player.getName(), ActionType.KICK, reason, null, false));

        src.sendMessage(getReplyText(PluginMessages.KICK_SUCCESSFUL, TextType.OK));

        return CommandResult.success();
    }
}
