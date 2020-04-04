package ru.allformine.afmbans.commands;

import com.google.gson.JsonObject;
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

public class CommandWarn extends Command {
    @Override
    public CommandResult execute(CommandSource src, CommandContext args) throws CommandException {
        Optional<String> protoNick = args.getOne("player");
        if (!protoNick.isPresent())
            throw new CommandException(getReplyText(PluginMessages.NOT_ENOUGH_ARGUMENTS, TextType.ERROR));
        String nick = PluginUtils.getTrueNickCase(protoNick.get());

        BanAPI banApi = new BanAPI(nick);

        String reason = args.<String>getOne("reason").orElse(PluginStatics.DEFAULT_REASON);

        JsonObject result;
        boolean ok;

        try {
            result = banApi.punish(src, PunishType.WARN, reason, null, null);
            ok = result.get("ok").getAsBoolean();
        } catch (Exception e) {
            throw new CommandException(getReplyText(PluginMessages.UNKNOWN_ERROR, TextType.ERROR));
        }

        if (!ok) throw new CommandException(getReplyText(PluginMessages.API_ERROR, TextType.ERROR));

        PluginStatics.broadcastChannel.send(PluginUtils.getBroadcastPunishMessage(src, nick, ActionType.WARN, reason, null, false));

        src.sendMessage(getReplyText(PluginMessages.WARN_SUCCESSFUL, TextType.OK));

        if (result.get("count").getAsInt() >= PluginStatics.MAX_WARNS) {
            PluginUtils.resetPlayerWarnsAndBan(nick, src);
        }

        return CommandResult.success();
    }
}
