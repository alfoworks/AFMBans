package ru.allformine.afmbans;

import org.spongepowered.api.Sponge;
import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.service.user.UserStorageService;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.text.format.TextColors;
import ru.allformine.afmbans.net.api.ban.response.CheckResponse;
import ru.allformine.afmbans.net.api.ban.response.object.Punish;
import ru.allformine.afmbans.time.Duration;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Optional;

public class PluginUtils {
    public static Text getPunishMessage(CommandSource src, String target, ActionType type) {
        String action = "";

        switch (type) {
            case BAN:
                action = "забанил";
                break;
            case KICK:
                action = "кикнул";
                break;
            case MUTE:
                action = "выдал мут";
                break;
            case WARN:
                action = "выдал предупреждение";
                break;

            case UNBAN:
                action = "разбанил";
                break;
            case UNMUTE:
                action = "снял мут";
                break;
            case UNWARN:
                action = "снял предупреждение";
                break;
        }

        return Text.builder()
                .append(Text.of(src.getName() + " ")).color(PluginStatics.MESSAGE_COLOR)
                .append(Text.of(action + " "))
                .append(Text.builder().append(Text.of("игрока ")).color(TextColors.RESET).build())
                .append(Text.of(target)).color(PluginStatics.MESSAGE_COLOR)
                .append(type != ActionType.UNBAN && type != ActionType.UNMUTE && type != ActionType.UNWARN ? Text.builder().append(Text.of(" навсегда ")).color(TextColors.RESET).build() : Text.of())
                .build();
    }

    public static Text getBanMessageForPlayer(String source, String reason) {
        return Text.builder()
                .append(Text.of("Вам"))
                .append(Text.builder().append(Text.of(" перманентный ")).color(PluginStatics.BAN_MESSAGE_COLOR).build())
                .append(Text.of("бан :3"))
                .append(Text.of("\n\n"))
                .append(Text.of("От: "))
                .append(Text.builder().append(Text.of(source)).color(PluginStatics.BAN_MESSAGE_COLOR).build())
                .append(Text.of("\n"))
                .append(Text.of("По причине: "))
                .append(Text.builder().append(Text.of(reason)).color(PluginStatics.BAN_MESSAGE_COLOR).build())
                .build();
    }

    public static String getOneLineBanMessageForPlayer(CheckResponse response) {
        Punish punish = response.reason;

        return String.format("Забанил: %s, по причине: %s", punish.source, punish.reason);
    }

    public static Text getPlayerTwinksMessage(String nickname, ArrayList<String> nicks) {
        return Text.builder()
                .append(Text.builder(nickname).color(PluginStatics.MESSAGE_COLOR).build())
                .append(Text.of(" вошёл в игру. Его остальные аккаунты: "))
                .append(Text.builder().append(Text.of(String.join("\n - ", nicks))).color(PluginStatics.MESSAGE_COLOR).build())
                .build();
    }

    public static void debug(String string) {
        if (PluginStatics.DEBUG_MODE) AFMBans.logger.warn(string);
    }

    public static String getTrueNickCase(String nickname) {
        Optional<UserStorageService> userStorage = Sponge.getServiceManager().provide(UserStorageService.class);

        if (userStorage.isPresent() && userStorage.get().get(nickname).isPresent()) {
            return userStorage.get().get(nickname).get().getName();
        }

        return nickname;
    }

    public static Duration getDuration(String unit, int time) {
        if (Arrays.stream(PluginStatics.TEMP_PUNISH_TIME_UNITS).noneMatch(unit::equalsIgnoreCase)) {
            throw new IllegalArgumentException("Unknown unit");
        }

        Duration dura;
        switch (unit) {
            case "s":
                dura = Duration.ofSeconds(time);
                break;
            case "m":
                dura = Duration.ofMinutes(time);
                break;
            case "h":
                dura = Duration.ofHours(time);
                break;
            case "d":
                dura = Duration.ofDays(time);
                break;
            case "w":
                dura = Duration.ofWeeks(time);
                break;
            case "M":
                dura = Duration.ofMonths(time);
                break;
            case "Y":
                dura = Duration.ofYears(time);
                break;
            default:
                dura = Duration.ZERO;
        }
        return dura;
    }
}
