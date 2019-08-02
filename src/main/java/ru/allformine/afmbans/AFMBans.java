package ru.allformine.afmbans;

import com.google.inject.Inject;
import org.slf4j.Logger;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.command.args.GenericArguments;
import org.spongepowered.api.command.spec.CommandSpec;
import org.spongepowered.api.event.Listener;
import org.spongepowered.api.event.game.state.GameLoadCompleteEvent;
import org.spongepowered.api.event.game.state.GamePreInitializationEvent;
import org.spongepowered.api.plugin.Plugin;
import org.spongepowered.api.text.Text;
import ru.allformine.afmbans.commands.CommandBan;
import ru.allformine.afmbans.commands.CommandCheckPlayer;
import ru.allformine.afmbans.commands.CommandDebugMode;
import ru.allformine.afmbans.commands.CommandUnban;
import ru.allformine.afmbans.listeners.BanEventListener;
import ru.allformine.afmbans.listeners.PlayerHistoryListener;

@Plugin(id = "afmbans", name = "AFMBans")
public class AFMBans {
    @Inject
    public static Logger logger;

    @Inject
    private void setLogger(Logger logger) {
        AFMBans.logger = logger;
    }

    @Listener
    public void onLoadComplete(GameLoadCompleteEvent event) {
        CommandSpec banSpec = CommandSpec.builder()
                .description(Text.of("Забанить игрока."))
                .permission(PluginPermissions.COMMAND_BAN)
                .arguments(
                        GenericArguments.onlyOne(GenericArguments.string(Text.of("player"))),
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

        Sponge.getCommandManager().register(this, banSpec, "ban", "afmban");
        Sponge.getCommandManager().register(this, unbanSpec, "unban", "afmunban");
        Sponge.getCommandManager().register(this, checkPlayerSpec, "checkplayer", "afmcheckplayer", "cp", "afmcp");
        Sponge.getCommandManager().register(this, debugModeSpec, "afmbansdebug", "debugmode", "dm", "afmdm");
    }

    @Listener
    public void preInit(GamePreInitializationEvent event) {
        Sponge.getEventManager().registerListeners(this, new BanEventListener());
        Sponge.getEventManager().registerListeners(this, new PlayerHistoryListener());
    }
}
