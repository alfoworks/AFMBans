package ru.allformine.afmbans;

import org.spongepowered.api.Sponge;
import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.text.format.TextColors;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

public class PluginUtils {
    public static DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSXXX");
    public static void broadcastPunishMessage(CommandSource src, String target, ActionType type) {
        String action = "";

        switch(type) {
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

        Text text = Text.builder()
                .append(Text.of(src.getName() + " ")).color(PluginStatics.MESSAGE_COLOR)
                .append(Text.of(action + " "))
                .append(Text.builder().append(Text.of("игрока ")).color(TextColors.RESET).build())
                .append(Text.of(target)).color(PluginStatics.MESSAGE_COLOR)
                .append(type != ActionType.UNBAN && type != ActionType.UNMUTE && type != ActionType.UNWARN ? Text.builder().append(Text.of(" навсегда ")).color(TextColors.RESET).build() : Text.of())
                .build();

        Sponge.getServer().getBroadcastChannel().send(text);
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
}
