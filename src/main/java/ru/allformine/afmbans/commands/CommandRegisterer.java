package ru.allformine.afmbans.commands;

import org.spongepowered.api.Sponge;
import org.spongepowered.api.command.args.GenericArguments;
import org.spongepowered.api.command.spec.CommandSpec;
import org.spongepowered.api.text.Text;
import ru.allformine.afmbans.PluginPermissions;

public class CommandRegisterer {
    public static void registerCommands(Object plugin){
        CommandSpec banSpec = CommandSpec.builder()
                .description(Text.of("Забанить игрока."))
                .permission(PluginPermissions.COMMAND_BAN)
                .arguments(
                        GenericArguments.onlyOne(GenericArguments.string(Text.of("player"))),
                        GenericArguments.optional(GenericArguments.string(Text.of("time"))),
                        GenericArguments.optional(GenericArguments.string(Text.of("unit"))),
                        GenericArguments.optional(GenericArguments.remainingJoinedStrings(Text.of("reason"))))
                .executor(new CommandBan())
                .build();

        CommandSpec unbanSpec = CommandSpec.builder()
                .description(Text.of("Разбанить игрока."))
                .permission(PluginPermissions.COMMAND_UNBAN)
                .arguments(
                        GenericArguments.onlyOne(GenericArguments.string(Text.of("player"))))
                .executor(new CommandUnban())
                .build();

        CommandSpec dupeipSpec = CommandSpec.builder()
                .description(Text.of("Проверить все IP игрока и его наказания."))
                .permission(PluginPermissions.COMMAND_DUPEIP)
                .arguments(
                        GenericArguments.optional(GenericArguments.string(Text.of("player"))),
                        GenericArguments.optional(GenericArguments.ip(Text.of("ip"))))
                .executor(new CommandDupeip())
                .build();

        CommandSpec debugModeSpec = CommandSpec.builder()
                .description(Text.of("Переключить режим отладки."))
                .permission(PluginPermissions.COMMAND_DEBUG_MODE)
                .executor(new CommandDebugMode())
                .build();

        CommandSpec debugSpec = CommandSpec.builder()
                .description(Text.of("Отъебись"))
                .permission(PluginPermissions.COMMAND_DEBUG)
                .executor(new CommandDebug())
                .build();

        Sponge.getCommandManager().register(plugin, banSpec, "ban", "afmban");
        Sponge.getCommandManager().register(plugin, unbanSpec, "unban", "afmunban");
        Sponge.getCommandManager().register(plugin, dupeipSpec, "dupeip");
        Sponge.getCommandManager().register(plugin, debugModeSpec, "afmbansdebug", "debugmode", "dm", "afmdm");
        Sponge.getCommandManager().register(plugin, debugSpec, "debug", "dbg", "d", "afmd");
    }
}
