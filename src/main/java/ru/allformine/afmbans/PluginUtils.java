package ru.allformine.afmbans;

import org.spongepowered.api.Sponge;
import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.text.format.TextColors;

public class PluginUtils {
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
                .append(Text.of(src.getName())).color(PluginStatics.BROADCAST_COLOR)
                .append(Text.of(action))
                .append(Text.builder().append(Text.of(" игрока")).color(TextColors.RESET).build())
                .append(Text.of(target)).color(PluginStatics.BROADCAST_COLOR)
                .append(Text.builder().append(Text.of(" навсегда")).color(TextColors.RESET).build())
                .build();

        Sponge.getServer().getBroadcastChannel().send(text);
    }

    public static Text getBanMessageForPlayer(String source, String reason) {
        Text message = Text.builder()
                .append(Text.of("Вам перманентный бан :3")).color(PluginStatics.BAN_MESSAGE_COLOR)
                .append(Text.of("\n\n"))
                .append(Text.builder().append(Text.of("От: ")).color(TextColors.RESET).build())
                .append(Text.of(source))
                .append(Text.of("\n"))
                .append(Text.builder().append(Text.of("По причине: ")).color(TextColors.RESET).build())
                .append(Text.of(reason)).color(PluginStatics.BAN_MESSAGE_COLOR)
                .build();

        return message;
    }
}
