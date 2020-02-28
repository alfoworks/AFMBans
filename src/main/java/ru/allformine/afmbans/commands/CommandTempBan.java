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
import ru.allformine.afmbans.net.api.ban.error.ApiException;
import ru.allformine.afmbans.time.Duration;

import java.io.IOException;
import java.net.InetAddress;
import java.util.Arrays;
import java.util.Date;
import java.util.Optional;

public class CommandTempBan extends Command {
    @Override
    public CommandResult execute(CommandSource src, CommandContext args) throws CommandException {
        Optional<String> nick = args.getOne("player");
        if (!nick.isPresent())
            throw new CommandException(getReplyText(PluginMessages.NOT_ENOUGH_ARGUMENTS, TextType.ERROR));

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
        if (args.hasAny("i")) {
            try {
                ip = PluginUtils.tryGetAddressForNick(nick.get());
            } catch (IOException | ApiException e) {
                throw new CommandException(getReplyText("Произошла ошибка при попытке получения IP игрока.", TextType.ERROR));
            }

            if (ip == null) {
                throw new CommandException(getReplyText("IP этого игрока не найден.", TextType.ERROR));
            }
        }

        String reason = args.<String>getOne("reason").orElse(PluginStatics.DEFAULT_REASON);
        Duration durka = PluginUtils.getDuration(unit, time.get());
        BanAPI banApi = new BanAPI(nick.get());

        boolean ok;

        try {
            ok = banApi.punish(src, PunishType.BAN, reason, durka, ip).get("ok").getAsBoolean();
        } catch (Exception e) {
            throw new CommandException(getReplyText(PluginMessages.UNKNOWN_ERROR, TextType.ERROR));
        }

        if (!ok) throw new CommandException(getReplyText(PluginMessages.API_ERROR, TextType.ERROR));

        Date end = new Date(System.currentTimeMillis() + durka.getSeconds() * 1000);

        Sponge.getServer().getPlayer(nick.get()).ifPresent(player -> player.kick(PluginUtils.getBanMessageForPlayer(src.getName(), reason, end)));

        src.sendMessage(getReplyText(PluginMessages.BAN_SUCCESSFUL, TextType.OK));
        PluginStatics.broadcastChannel.send(PluginUtils.getPunishMessage(src, nick.get(), ActionType.BAN, reason, PluginUtils.getDuratioPluralized(unit, time.get())));

        return CommandResult.success();
    }
}
