package ru.allformine.afmbans;

import com.google.inject.Inject;
import org.slf4j.Logger;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.event.Listener;
import org.spongepowered.api.event.game.state.GameLoadCompleteEvent;
import org.spongepowered.api.event.game.state.GamePreInitializationEvent;
import org.spongepowered.api.event.game.state.GameStartedServerEvent;
import org.spongepowered.api.plugin.Plugin;
import org.spongepowered.api.scheduler.Task;
import ru.allformine.afmbans.commands.registering.CommandRegisterer;
import ru.allformine.afmbans.listeners.BanEventListener;
import ru.allformine.afmbans.listeners.MuteListener;
import ru.allformine.afmbans.listeners.PlayerHistoryListener;

import java.util.concurrent.TimeUnit;

@Plugin(id = "afmbans", name = "AFMBans", description = "Кастомные баны")
public class AFMBans {
    @Inject
    public static Logger logger;

    @Inject
    private void setLogger(Logger logger) {
        AFMBans.logger = logger;
    }

    @Listener
    public void onLoadComplete(GameLoadCompleteEvent event) {
        CommandRegisterer.registerCommands(this);
    }

    @Listener
    public void preInit(GamePreInitializationEvent event) {
        Sponge.getEventManager().registerListeners(this, new BanEventListener());
        Sponge.getEventManager().registerListeners(this, new PlayerHistoryListener());
        Sponge.getEventManager().registerListeners(this, new MuteListener());
    }

    @Listener
    public void started(GameStartedServerEvent event) {
        logger.trace("Starting mute info task...");

        Task.builder().execute(new MuteInfoTask()).interval(1L, TimeUnit.MINUTES).async().submit(this);
    }
}
