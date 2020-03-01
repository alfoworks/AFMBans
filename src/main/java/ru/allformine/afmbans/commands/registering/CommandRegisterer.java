package ru.allformine.afmbans.commands.registering;

import org.spongepowered.api.Sponge;
import org.spongepowered.api.command.args.GenericArguments;
import org.spongepowered.api.command.spec.CommandSpec;
import org.spongepowered.api.text.Text;
import ru.allformine.afmbans.PluginPermissions;
import ru.allformine.afmbans.commands.*;

public class CommandRegisterer {
    public static void registerCommands(Object plugin) {
        CommandSpec banSpec = CommandSpec.builder()
                .description(Text.of("Забанить игрока навсегда."))
                .permission(PluginPermissions.COMMAND_BAN)
                .arguments(
                        Parameters.IP_FLAG.buildWith(GenericArguments.seq(
                                Parameters.PLAYER_AS_STRING,
                                Parameters.OPTIONAL_REASON
                        )))
                .executor(new CommandBan())
                .build();

        CommandSpec tempBanSpec = CommandSpec.builder()
                .description(Text.of("Временно забанить игрока."))
                .permission(PluginPermissions.COMMAND_TEMP_BAN)
                .arguments(
                        Parameters.IP_FLAG.buildWith(GenericArguments.seq(
                                Parameters.PLAYER_AS_STRING,
                                Parameters.TIME_COUNT,
                                Parameters.TIME_UNIT,
                                Parameters.OPTIONAL_REASON)))
                .executor(new CommandTempBan())
                .build();

        CommandSpec unbanSpec = CommandSpec.builder()
                .description(Text.of("Разбанить игрока."))
                .permission(PluginPermissions.COMMAND_UNBAN)
                .arguments(
                        Parameters.PLAYER_AS_STRING)
                .executor(new CommandUnban())
                .build();

        CommandSpec dupeipSpec = CommandSpec.builder()
                .description(Text.of("Проверить все IP игрока и его наказания."))
                .permission(PluginPermissions.COMMAND_DUPEIP)
                .arguments(
                        Parameters.OPTIONAL_PLAYER,
                        Parameters.OPTIONAL_IP)
                .executor(new CommandDupeip())
                .build();

        CommandSpec debugSpec = CommandSpec.builder()
                .description(Text.of("Дебаг плагина"))
                .permission(PluginPermissions.COMMAND_DEBUG)
                .executor(new CommandDebug())
                .child(new CommandDebugMethod(), "method")
                .child(new CommandDebugRequests(), "requests")
                .build();

        CommandSpec kickSpec = CommandSpec.builder()
                .description(Text.of("Кикнуть игрока."))
                .permission(PluginPermissions.COMMAND_KICK)
                .executor(new CommandKick())
                .arguments(
                        Parameters.PLAYER,
                        Parameters.OPTIONAL_REASON)
                .build();

        Sponge.getCommandManager().register(plugin, banSpec, "ban", "afmban");
        Sponge.getCommandManager().register(plugin, tempBanSpec, "tempban", "afmtempban");
        Sponge.getCommandManager().register(plugin, unbanSpec, "unban", "afmunban");
        Sponge.getCommandManager().register(plugin, kickSpec, "kick", "afmkick");
        Sponge.getCommandManager().register(plugin, dupeipSpec, "dupeip");
        Sponge.getCommandManager().register(plugin, debugSpec, "debug", "dbg", "d", "afmd");
    }
}
