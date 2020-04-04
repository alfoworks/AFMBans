package ru.allformine.afmbans;

import org.spongepowered.api.Sponge;
import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.service.user.UserStorageService;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.text.format.TextColors;
import ru.allformine.afmbans.net.api.ban.BanAPI;
import ru.allformine.afmbans.net.api.ban.PunishType;
import ru.allformine.afmbans.net.api.ban.error.ApiException;
import ru.allformine.afmbans.net.api.ban.response.IpHistoryResponse;
import ru.allformine.afmbans.net.api.ban.response.object.IpHistoryRecord;
import ru.allformine.afmbans.time.Duration;

import javax.annotation.Nullable;
import java.io.IOException;
import java.net.InetAddress;
import java.text.ParseException;
import java.util.*;
import java.util.stream.Collectors;

public class PluginUtils {
    public static Text getBroadcastPunishMessage(CommandSource src, String target, ActionType type, @Nullable String reason, @Nullable String pluralizedDuration, boolean ip) {
        String action = "";
        List<ActionType> alterCaseTypes = Arrays.asList(ActionType.MUTE, ActionType.UNMUTE, ActionType.WARN, ActionType.UNWARN);

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

        String durationString = pluralizedDuration != null ? "на " + pluralizedDuration : "навсегда";

        Text.Builder text = Text.builder()
                .append(Text.of(src.getName() + " ")).color(PluginStatics.MESSAGE_COLOR)
                .append(Text.of(action + " "))
                .append(Text.builder().append(Text.of(alterCaseTypes.contains(type) ? "игроку " : " игрока ")).color(TextColors.RESET).build())
                .append(Text.of(target)).color(PluginStatics.MESSAGE_COLOR);

        if (ip) {
            text.append(Text.builder()
                    .append(Text.of(" по")).color(TextColors.RESET).build())
                    .append(Text.builder().append(Text.of(" IP")).color(PluginStatics.MESSAGE_COLOR).build());
        }

        text.append(type != ActionType.UNBAN && type != ActionType.UNMUTE && type != ActionType.UNWARN ? Text.builder().append(Text.of(String.format(" %s ", durationString))).color(TextColors.RESET).build() : Text.of());

        if (reason != null) {
            text.append(Text.builder().append(Text.of("по причине ")).color(TextColors.RESET).build())
                    .append(Text.builder().append(Text.of(String.format("\"%s\"", reason))).color(PluginStatics.MESSAGE_COLOR).build());
        }

        return text.build();
    }

    public static void sendMuteMessage(Player player) {
        StringBuilder message = new StringBuilder();
        Date deadline = MuteCache.getPlayerMuteDeadline(player);

        message.append("Вы не можете отправлять сообщения, т.к. вы находитесь в ");
        message.append(deadline == null ? "перманентном муте" : "муте до " + deadline.toString());
        message.append("!");

        player.sendMessage(Text.builder().append(Text.of(message.toString())).color(TextColors.RED).build());
    }

    public static Text getDupeIpText(Map<String, Boolean> results) {
        Text.Builder builder = Text.builder();

        results.forEach((nickname, punished) -> {
            boolean isMuted;
            int warnCount;

            /*
            В данном случае нет обращения к кешу, т.к. запрос не интервальный
             */

            BanAPI api = new BanAPI(nickname);

            try {
                isMuted = api.check(PunishType.MUTE, null).punished;
                warnCount = api.check(PunishType.WARN, null).count;
            } catch (Exception e) {
                AFMBans.logger.error(String.format("Failed to check %s mute or warns while generating DupeIP output!!!", nickname));
                e.printStackTrace();
                return;
            }

            builder
                    .append(Text.builder().append(Text.of(nickname)).color(TextColors.GOLD).build())
                    .append(Text.builder().append(Text.of(" [ ")).color(TextColors.RESET).build())
                    .append(Text.builder().append(Text.of(punished ? "Забанен" : "Не забанен")).color(punished ? TextColors.RED : TextColors.GREEN).build())
                    .append(Text.builder().append(Text.of(" ]")).color(TextColors.RESET).build())
                    .append(Text.builder().append(Text.of(" [ ")).build())
                    .append(Text.builder().append(Text.of(isMuted ? "В муте" : "Не в муте")).color(isMuted ? TextColors.RED : TextColors.GREEN).build())
                    .append(Text.builder().append(Text.of(" ]")).color(TextColors.RESET).build())
                    .append(Text.builder().append(Text.of(" [ ")).build())
                    .append(Text.builder().append(Text.of(String.format("Ворны: %s", warnCount))).color(warnCount > 0 ? TextColors.RED : TextColors.GREEN).build())
                    .append(Text.builder().append(Text.of(" ]")).color(TextColors.RESET).build())
                    .build();
        });

        return builder.build();
    }

    public static void resetPlayerWarnsAndBan(String nick, CommandSource source) {
        BanAPI api = new BanAPI(nick);

        for (int i = 0; i < PluginStatics.MAX_WARNS; i++) {
            try {
                api.amnesty(source, PunishType.WARN);
            } catch (IOException | ApiException e) {
                AFMBans.logger.error(String.format("Error removing warn #%s from player %s!", i + 1, nick));
                e.printStackTrace();
            }
        }

        String reason = String.format("Превышено максимальное кол-во предупреждений: %s", PluginStatics.MAX_WARNS);
        int days = 3;
        Duration duration = Duration.ofDays(days);

        try {
            api.punish(source, PunishType.BAN, reason, duration, null);
        } catch (IOException | ApiException e) {
            AFMBans.logger.error(String.format("Error banning player %s for max warns.", nick));
            e.printStackTrace();
        }

        Sponge.getServer().getPlayer(nick).ifPresent(player -> player.kick(PluginUtils.getPunishMessageForPlayer(PunishType.BAN, source.getName(), reason, new Date(System.currentTimeMillis() + duration.getSeconds() * 1000))));

        PluginStatics.broadcastChannel.send(PluginUtils.getBroadcastPunishMessage(source, nick, ActionType.BAN, reason, PluginUtils.getDuratioPluralized("d", days), false));
    }

    public static Text getPunishMessageForPlayer(PunishType type, String source, String reason, @Nullable Date date) {
        if (type != PunishType.KICK && type != PunishType.BAN) {
            throw new IllegalArgumentException();
        }

        Text.Builder text = Text.builder()
                .append(Text.of("Вам "));

        if (type == PunishType.BAN) {
            text.append(Text.builder().append(Text.of(date == null ? "перманентный " : "временный ")).color(PluginStatics.BAN_MESSAGE_COLOR).build())
                    .append(Text.of("бан :3"));
        } else {
            text.append(Text.builder().append(Text.of("кик :3")).color(PluginStatics.BAN_MESSAGE_COLOR).build());
        }

        text.append(Text.of("\n\n"))
                .append(Text.of("От: "))
                .append(Text.builder().append(Text.of(source)).color(PluginStatics.BAN_MESSAGE_COLOR).build())
                .append(Text.of("\n"))
                .append(Text.of("По причине: "))
                .append(Text.builder().append(Text.of(reason)).color(PluginStatics.BAN_MESSAGE_COLOR).build());

        if (date != null) {
            text.append(Text.of("\n"));
            text.append(Text.of("До: "))
                    .append(Text.builder().append(Text.of(date.toString())).color(PluginStatics.BAN_MESSAGE_COLOR).build());
        }

        if (type != PunishType.KICK) text.append(Text.of("\n\n")).append(PluginStatics.additionalBanMessage);

        return text.build();
    }

    /* Пока не нужно
    public static String getOneLineBanMessageForPlayer(CheckResponse response) {
        Punish punish = response.reason;

        return String.format("Забанил: %s, по причине: %s", punish.source, punish.reason);
    }
     */

    public static Text getPlayerTwinksMessage(String nickname, List<String> nicks) {
        return Text.builder()
                .append(Text.builder(nickname).color(PluginStatics.MESSAGE_COLOR).build())
                .append(Text.of(" вошёл в игру. Его остальные аккаунты: "))
                .append(Text.builder().append(Text.of(join(nicks, "\n -"))).color(PluginStatics.MESSAGE_COLOR).build())
                .build();
    }

    public static void debug(String string) {
        if (PluginStatics.DEBUG_MODE) AFMBans.logger.warn(string);
    }

    public static String getTrueNickCase(String nickname) {
        /* Сначала чекаем локально, поможет лишний раз не обращаться к API */

        Optional<UserStorageService> userStorage = Sponge.getServiceManager().provide(UserStorageService.class);

        if (userStorage.isPresent() && userStorage.get().get(nickname).isPresent()) {
            return userStorage.get().get(nickname).get().getName();
        }

        /* В таком случае можно уже и у базы узнать */

        BanAPI banApi = new BanAPI(nickname);
        try {
            return banApi.check(PunishType.BAN, null).target;
        } catch (IOException | ParseException | ApiException e) {
            AFMBans.logger.error("API error occurred when was trying to get true nick case:");
            e.printStackTrace();
        }

        /* Не нашло, тогда возвращаем то, в чем указали */

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
            case "mo":
                dura = Duration.ofMonths(time);
                break;
            case "y":
                dura = Duration.ofYears(time);
                break;
            default:
                dura = Duration.ZERO;
        }

        return dura;
    }

    public static String getDuratioPluralized(String unit, int time) {
        String pluralizedWord;

        switch (unit) {
            case "s":
                pluralizedWord = pluralize(time, "секунда", "секунды", "секунд");
                break;
            case "m":
                pluralizedWord = pluralize(time, "минута", "минуты", "минут");
                break;
            case "h":
                pluralizedWord = pluralize(time, "час", "часа", "часов");
                break;
            case "d":
                pluralizedWord = pluralize(time, "день", "дня", "дней");
                break;
            case "w":
                pluralizedWord = pluralize(time, "неделю", "недели", "недель");
                break;
            case "mo":
                pluralizedWord = pluralize(time, "месяц", "месяца", "месяцев");
                break;
            case "y":
                pluralizedWord = pluralize(time, "год", "года", "лет");
                break;
            default:
                pluralizedWord = pluralize(time, "хуй", "хуя", "хуёв");
        }

        return String.format("%s %s", time, pluralizedWord);
    }

    public static InetAddress tryGetAddressForNick(final String nick) throws IOException, ApiException {
        final Optional<Player> playerOptional = Sponge.getServer().getPlayer(nick);

        if (playerOptional.isPresent()) {
            return playerOptional.get().getConnection().getAddress().getAddress();
        }

        IpHistoryResponse response = BanAPI.getIpHistory(nick, null);
        if (response.items.size() == 0) {
            return null;
        }

        IpHistoryRecord lastItem = response.items.get(response.items.size() - 1);

        return lastItem.ip;
    }

    public static String join(Collection<String> collection, String symbol) {
        return collection.stream().collect(Collectors.joining(symbol, symbol, symbol));
    }

    // ============================== //

    private static String pluralize(int number, String nomSing, String genSing, String genPl) {
        String numberString = String.valueOf(number);
        int lastDigit = Integer.parseInt(numberString.substring(numberString.length() - 1));
        int lastTwoDigits = numberString.length() > 1 ? Integer.parseInt(numberString.substring(numberString.length() - 2)) : lastDigit;

        if (lastTwoDigits >= 11 && lastTwoDigits <= 19) {
            return genPl;
        } else if (lastDigit == 1) {
            return nomSing;
        } else if (lastDigit >= 2 && lastDigit <= 4) {
            return genSing;
        } else {
            return genPl;
        }
    }
}
