package ru.allformine.afmbans;

import com.google.inject.Inject;
import org.slf4j.Logger;
import org.spongepowered.api.event.Listener;
import org.spongepowered.api.event.game.state.GameStartedServerEvent;
import org.spongepowered.api.event.network.ClientConnectionEvent;
import org.spongepowered.api.plugin.Plugin;

@Plugin(
        id = "afmbans",
        name = "AFMBans",
        description = "Custom ban plugin.",
        url = "allformine.ru",
        authors = {
                "Iterator",
                "HeroBrine1st_Erq"
        }
)
public class AFMBans {
    @Inject
    public static Logger logger;
    @Inject
    private void setLogger(Logger logger){
        AFMBans.logger = logger;
    }

    @Listener
    public void onServerStart(GameStartedServerEvent event) {

    }

    @Listener
    public void onPlayerLogin(ClientConnectionEvent.Login event){

    }
}
