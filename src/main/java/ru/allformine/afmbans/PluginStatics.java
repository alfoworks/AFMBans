package ru.allformine.afmbans;

import org.spongepowered.api.text.format.TextColor;
import org.spongepowered.api.text.format.TextColors;

import java.text.DateFormat;
import java.text.SimpleDateFormat;

public class PluginStatics {
    public static String DEFAULT_REASON = "Плохое поведение";

    public static TextColor MESSAGE_COLOR = TextColors.GREEN;
    public static TextColor BAN_MESSAGE_COLOR = TextColors.LIGHT_PURPLE;

    public static boolean DEBUG_MODE = false;
    public static DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSXXX");
}
