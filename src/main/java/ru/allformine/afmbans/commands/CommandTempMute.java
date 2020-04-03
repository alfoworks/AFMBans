package ru.allformine.afmbans.commands;

import org.spongepowered.api.Sponge;
import org.spongepowered.api.command.CommandException;
import org.spongepowered.api.command.CommandResult;
import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.command.args.CommandContext;
import ru.allformine.afmbans.*;
import ru.allformine.afmbans.net.api.ban.BanAPI;
import ru.allformine.afmbans.net.api.ban.PunishType;
import ru.allformine.afmbans.net.api.ban.error.ApiException;
import ru.allformine.afmbans.time.Duration;

import java.io.IOException;
import java.net.InetAddress;
import java.util.Arrays;
import java.util.Optional;

public class CommandTempMute extends Command {
    @Override
    public CommandResult execute(CommandSource src, CommandContext args) throws CommandException {
        Optional<String> protoNick = args.getOne("player");
        if (!protoNick.isPresent())
            throw new CommandException(getReplyText(PluginMessages.NOT_ENOUGH_ARGUMENTS, TextType.ERROR));

        String nick = PluginUtils.getTrueNickCase(protoNick.get());

        Optional<Integer> time = args.getOne("time");
        Optional<String> protoUnit = args.getOne("unit");

        if (!time.isPresent() || !protoUnit.isPresent()) {
            throw new CommandException(getReplyText(PluginMessages.NOT_ENOUGH_ARGUMENTS, TextType.ERROR));
        }

        String unit = protoUnit.get().toLowerCase();

        if (time.get() < 0) throw new CommandException(getReplyText("Укажите число > 0.", TextType.ERROR));

        if (Arrays.stream(PluginStatics.TEMP_PUNISH_TIME_UNITS).noneMatch(unit::equals)) {
            throw new CommandException(getReplyText("Неверная еденица времени, доступные единицы: " + String.join(", ", PluginStatics.TEMP_PUNISH_TIME_UNITS), TextType.ERROR));
        }

        InetAddress ip = null;
        if (args.hasAny("ip")) {
            try {
                ip = PluginUtils.tryGetAddressForNick(nick);
            } catch (IOException | ApiException e) {
                throw new CommandException(getReplyText(PluginMessages.API_ERROR, TextType.ERROR));
            }

            if (ip == null) {
                throw new CommandException(getReplyText(PluginMessages.PLAYER_IP_NOT_FOUND, TextType.ERROR));
            }
        }

        String reason = args.<String>getOne("reason").orElse(PluginStatics.DEFAULT_REASON);
        Duration durka = PluginUtils.getDuration(unit, time.get());
        BanAPI banApi = new BanAPI(nick);

        boolean ok;

        try {
            ok = banApi.punish(src, PunishType.MUTE, reason, durka, ip).get("ok").getAsBoolean();
        } catch (Exception e) {
            throw new CommandException(getReplyText(PluginMessages.UNKNOWN_ERROR, TextType.ERROR));
        }

        if (!ok) throw new CommandException(getReplyText(PluginMessages.API_ERROR, TextType.ERROR));

        Sponge.getServer().getPlayer(nick).ifPresent(player -> MuteCache.setPlayerMuted(player, true));

        src.sendMessage(getReplyText(ip == null ? PluginMessages.TEMPMUTE_SUCESSFUL : PluginMessages.IPTEMPMUTE_SUCCESSFUL, TextType.OK));
        PluginStatics.broadcastChannel.send(PluginUtils.getBroadcastPunishMessage(src, nick, ActionType.MUTE, reason, PluginUtils.getDuratioPluralized(unit, time.get()), ip != null));

        return CommandResult.success();
    }
}
