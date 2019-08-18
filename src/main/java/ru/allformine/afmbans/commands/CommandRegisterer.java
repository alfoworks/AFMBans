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

        CommandSpec checkPlayerSpec = CommandSpec.builder()
                .description(Text.of("Проверить все IP игрока и его наказания."))
                .permission(PluginPermissions.COMMAND_CHECK_PLAYER)
                .arguments(
                        GenericArguments.onlyOne(GenericArguments.string(Text.of("player"))))
                .executor(new CommandCheckPlayer())
                .build();

        CommandSpec debugModeSpec = CommandSpec.builder()
                .description(Text.of("Переключить режим отладки."))
                .permission(PluginPermissions.COMMAND_DEBUG_MODE)
                .executor(new CommandDebugMode())
                .build();

        Sponge.getCommandManager().register(plugin, banSpec, "ban", "afmban");
        Sponge.getCommandManager().register(plugin, unbanSpec, "unban", "afmunban");
        Sponge.getCommandManager().register(plugin, checkPlayerSpec, "checkplayer", "afmcheckplayer", "cp", "afmcp");
        Sponge.getCommandManager().register(plugin, debugModeSpec, "afmbansdebug", "debugmode", "dm", "afmdm");
    }
}
