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
import java.util.Optional;

public class CommandTempBan extends Command {
    @Override
    public CommandResult execute(CommandSource src, CommandContext args) throws CommandException {
        Optional<String> nick = args.getOne("player");
        if (!nick.isPresent())
            throw new CommandException(getReplyText(PluginMessages.NOT_ENOUGH_ARGUMENTS, TextType.ERROR));

        Optional<Integer> time = args.getOne("time");
        Optional<String> unit = args.getOne("unit");

        if (!time.isPresent() || !unit.isPresent()) {
            throw new CommandException(getReplyText(PluginMessages.NOT_ENOUGH_ARGUMENTS, TextType.ERROR));
        }

        if (time.get() < 0) throw new CommandException(getReplyText("Укажите число > 0.", TextType.ERROR));

        if (Arrays.stream(PluginStatics.TEMP_PUNISH_TIME_UNITS).noneMatch(unit.get()::equalsIgnoreCase)) {
            throw new CommandException(getReplyText("Неверная еденица времени, доступные единицы: " + String.join(", ", PluginStatics.TEMP_PUNISH_TIME_UNITS), TextType.ERROR));
        }

        InetAddress ip = null;
        if (args.hasAny("ip")) {
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
        Duration durka = PluginUtils.getDuration(unit.get(), time.get());
        BanAPI banApi = new BanAPI(nick.get());

        boolean ok;

        try {
            ok = banApi.punish(src, PunishType.BAN, reason, durka, ip).get("ok").getAsBoolean();
        } catch (Exception e) {
            throw new CommandException(getReplyText(PluginMessages.UNKNOWN_ERROR, TextType.ERROR));
        }

        if (!ok) throw new CommandException(getReplyText(PluginMessages.API_ERROR, TextType.ERROR));

        Sponge.getServer().getPlayer(nick.get()).ifPresent(player -> player.kick(PluginUtils.getBanMessageForPlayer(src.getName(), reason)));

        src.sendMessage(getReplyText(PluginMessages.BAN_SUCCESSFUL, TextType.OK));
        PluginStatics.broadcastChannel.send(PluginUtils.getPunishMessage(src, nick.get(), ActionType.BAN));

        return CommandResult.success();
    }
}
