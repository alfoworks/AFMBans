package ru.allformine.afmbans.commands;

import org.spongepowered.api.Sponge;
import org.spongepowered.api.command.CommandException;
import org.spongepowered.api.command.CommandResult;
import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.command.args.CommandContext;
import org.spongepowered.api.entity.living.player.Player;
import ru.allformine.afmbans.PluginMessages;
import ru.allformine.afmbans.PluginStatics;
import ru.allformine.afmbans.PluginUtils;
import ru.allformine.afmbans.net.api.ban.BanAPI;
import ru.allformine.afmbans.net.api.ban.PunishType;

public class CommandBan extends Command {
    @Override
    public CommandResult execute(CommandSource src, CommandContext args) throws CommandException {
        String nick = args.<String>getOne("player").get();
        BanAPI banApi = new BanAPI(nick);

        String reason = (String) args.getOne("reason").orElse(PluginStatics.DEFAULT_REASON);

        boolean ok;

        try {
            ok = banApi.punish(src, PunishType.Ban, reason, null, null).get("ok").getAsBoolean();
        } catch (Exception e) {
            throw new CommandException(getReplyText(PluginMessages.UNKNOWN_ERROR, TextType.ERROR));
        }

        if (!ok) throw new CommandException(getReplyText(PluginMessages.API_ERROR, TextType.ERROR));

        Player player = Sponge.getServer().getPlayer(nick).orElse(null);

        if (player != null) {
            player.kick(PluginUtils.getBanMessageForPlayer(src.getName(), reason));
        }

        src.sendMessage(getReplyText(PluginMessages.BAN_SUCCESSFUL, TextType.OK));
        PluginUtils.broadcastPunishMessage(src, nick, PunishType.Ban);

        return CommandResult.success();
    }
}
