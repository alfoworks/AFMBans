package ru.allformine.afmbans.commands;

import org.spongepowered.api.Sponge;
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

import java.util.Optional;

public class CommandBan extends Command {
    @Override
    public CommandResult execute(CommandSource src, CommandContext args) throws CommandException {
        Optional<String> protoNick = args.getOne("player");
        if (!protoNick.isPresent())
            throw new CommandException(getReplyText(PluginMessages.NOT_ENOUGH_ARGUMENTS, TextType.ERROR));
        String nick = protoNick.get();
        BanAPI banApi = new BanAPI(nick);

        String reason = args.<String>getOne("reason").orElse(PluginStatics.DEFAULT_REASON);

        boolean ok;

        try {
            ok = banApi.punish(src, PunishType.BAN, reason, null, null).get("ok").getAsBoolean();
        } catch (Exception e) {
            throw new CommandException(getReplyText(PluginMessages.UNKNOWN_ERROR, TextType.ERROR));
        }

        if (!ok) throw new CommandException(getReplyText(PluginMessages.API_ERROR, TextType.ERROR));

        Sponge.getServer().getPlayer(nick).ifPresent(player -> player.kick(PluginUtils.getBanMessageForPlayer(src.getName(), reason)));

        src.sendMessage(getReplyText(PluginMessages.BAN_SUCCESSFUL, TextType.OK));
        PluginStatics.broadcastChannel.send(PluginUtils.getPunishMessage(src, nick, ActionType.BAN));

        return CommandResult.success();
    }
}
