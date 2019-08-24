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
import ru.allformine.afmbans.time.Duration;

import java.util.Arrays;
import java.util.Optional;

public class CommandBan extends Command {
    @Override
    public CommandResult execute(CommandSource src, CommandContext args) throws CommandException {
        Optional<String> protoNick = args.getOne("player");
        if (!protoNick.isPresent())
            throw new CommandException(getReplyText(PluginMessages.NOT_ENOUGH_ARGUMENTS, TextType.ERROR));
        String nick = protoNick.get();
        BanAPI banApi = new BanAPI(nick);

        Optional<String> time = args.getOne("time");
        Optional<String> unit = args.getOne("unit");

        Duration duration = null;

        if (time.isPresent() && unit.isPresent()) {
            // Темпбан

            int timeInt;

            try {
                timeInt = Integer.parseInt(time.get());
            } catch (NumberFormatException e) {
                throw new CommandException(getReplyText("Вы указали нечисловое значение.", TextType.ERROR));
            }

            if (timeInt < 0) throw new CommandException(getReplyText("Укажите число > 0.", TextType.ERROR));

            if (Arrays.stream(PluginStatics.TEMP_PUNISH_TIME_UNITS).noneMatch(unit.get()::equalsIgnoreCase)) {
                throw new CommandException(getReplyText("Неверная еденица времени, доступные единицы: " + String.join(", ", PluginStatics.TEMP_PUNISH_TIME_UNITS), TextType.ERROR));
            }
        } else if (time.isPresent() || unit.isPresent()) {
            // Что-то не указано для темпбана

            throw new CommandException(getReplyText("Укажите время в формате <количество> <единица времени>", TextType.ERROR));
        }

        String reason = args.<String>getOne("reason").orElse(PluginStatics.DEFAULT_REASON);

        boolean ok;

        try {
            ok = banApi.punish(src, PunishType.BAN, reason, duration, null).get("ok").getAsBoolean();
        } catch (Exception e) {
            throw new CommandException(getReplyText(PluginMessages.UNKNOWN_ERROR, TextType.ERROR));
        }

        if (!ok) throw new CommandException(getReplyText(PluginMessages.API_ERROR, TextType.ERROR));

        Sponge.getServer().getPlayer(nick).ifPresent(player -> player.kick(PluginUtils.getBanMessageForPlayer(src.getName(), reason)));

        src.sendMessage(getReplyText(PluginMessages.BAN_SUCCESSFUL, TextType.OK));
        Sponge.getServer().getBroadcastChannel().send(PluginUtils.getPunishMessage(src, nick, ActionType.BAN));

        return CommandResult.success();
    }
}
