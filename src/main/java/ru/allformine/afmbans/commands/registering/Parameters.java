package ru.allformine.afmbans.commands.registering;

import org.spongepowered.api.command.args.CommandElement;
import org.spongepowered.api.command.args.CommandFlags;
import org.spongepowered.api.command.args.GenericArguments;
import org.spongepowered.api.text.Text;

public class Parameters {
    public static CommandElement PLAYER_AS_STRING = GenericArguments.onlyOne(GenericArguments.string(Text.of("player")));
    public static CommandElement PLAYER = GenericArguments.onlyOne(GenericArguments.player(Text.of("player")));
    public static CommandElement OPTIONAL_REASON = GenericArguments.optional(GenericArguments.remainingJoinedStrings(Text.of("reason")));
    public static CommandElement TIME_COUNT = GenericArguments.onlyOne(GenericArguments.integer(Text.of("time")));
    public static CommandElement TIME_UNIT = GenericArguments.onlyOne(GenericArguments.string(Text.of("unit")));
    public static CommandElement OPTIONAL_PLAYER = GenericArguments.optional(GenericArguments.string(Text.of("player")));
    public static CommandElement OPTIONAL_IP = GenericArguments.optional(GenericArguments.ip(Text.of("ip")));

    public static CommandFlags.Builder IP_FLAG = GenericArguments.flags().flag("-ip", "i");
}
