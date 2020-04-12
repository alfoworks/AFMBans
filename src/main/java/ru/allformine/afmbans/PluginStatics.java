package ru.allformine.afmbans;

import org.spongepowered.api.text.Text;
import org.spongepowered.api.text.channel.MessageChannel;
import org.spongepowered.api.text.format.TextColor;
import org.spongepowered.api.text.format.TextColors;
import ru.allformine.afmbans.channels.NotifyChannel;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.List;

public class PluginStatics {
    public static String DEFAULT_REASON = "Плохое поведение";
    public static int MAX_WARNS = 5;

    public static TextColor MESSAGE_COLOR = TextColors.GREEN;
    public static TextColor BAN_MESSAGE_COLOR = TextColors.LIGHT_PURPLE;

    public static boolean DEBUG_MODE = false;
    public static DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

    public static String[] TEMP_PUNISH_TIME_UNITS = new String[]{"s", "m", "d", "w", "mo", "y"};

    public static List<String> prohibitedMuteCommands = Arrays.asList("g", "t", "l", "msg", "tell", "bc"); //FIXME дополни меня

    public static MessageChannel broadcastChannel = MessageChannel.TO_ALL;

    public static MessageChannel getNotifyChannel() { // Включает в себя всех, у кого есть перм на нотифи и консоль.
        return MessageChannel.combined(MessageChannel.TO_CONSOLE,
                MessageChannel.permission(PluginPermissions.PLAYER_JOIN_NOTIFY),
                new NotifyChannel());
    }

    public static Text additionalBanMessage = Text.builder()
            .append(Text.of("Хотите оправдать свой бан?"))
            .append(Text.of("\nВы можете обратиться к нам: "))
            .append(Text.builder().append(Text.of("https://mine.alfo.ws/support/")).color(BAN_MESSAGE_COLOR).build())
            .build();
}
