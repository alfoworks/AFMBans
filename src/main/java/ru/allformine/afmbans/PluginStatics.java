package ru.allformine.afmbans;

import org.spongepowered.api.text.channel.MessageChannel;
import org.spongepowered.api.text.format.TextColor;
import org.spongepowered.api.text.format.TextColors;
import ru.allformine.afmbans.channels.NotifyChannel;

import java.text.DateFormat;
import java.text.SimpleDateFormat;

public class PluginStatics {
    public static String DEFAULT_REASON = "Плохое поведение";

    public static TextColor MESSAGE_COLOR = TextColors.GREEN;
    public static TextColor BAN_MESSAGE_COLOR = TextColors.LIGHT_PURPLE;

    public static boolean DEBUG_MODE = false;
    public static DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSXXX");

    public static String[] TEMP_PUNISH_TIME_UNITS = new String[]{"s", "m", "d", "w", "mo", "y"};

    public static MessageChannel broadcastChannel = MessageChannel.TO_ALL;

    public static MessageChannel getNotifyChannel() { // Включает в себя всех, у кого есть перм на нотифи и консоль.
        return MessageChannel.combined(MessageChannel.TO_CONSOLE,
                MessageChannel.permission(PluginPermissions.PLAYER_JOIN_NOTIFY),
                new NotifyChannel());
    }
}
