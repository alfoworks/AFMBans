package ru.allformine.afmbans;

import org.spongepowered.api.Sponge;
import org.spongepowered.api.entity.living.player.Player;
import ru.allformine.afmbans.net.api.ban.BanAPI;
import ru.allformine.afmbans.net.api.ban.PunishType;
import ru.allformine.afmbans.net.api.ban.error.ApiException;

import java.io.IOException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class MuteInfoTask implements Runnable {
    public static final List<Player> mutedPlayers = Collections.synchronizedList(new ArrayList<>());

    public static void updateCache() {
        synchronized (mutedPlayers) {
            mutedPlayers.clear();

            for (Player player : Sponge.getServer().getOnlinePlayers()) {
                BanAPI api = new BanAPI(player.getName());
                try {
                    if (api.check(PunishType.MUTE, null).punished) {
                        mutedPlayers.add(player);
                    }
                } catch (IOException | ParseException | ApiException e) {
                    AFMBans.logger.error("Error checking player for mute:");
                    e.printStackTrace();
                }
            }
        }
    }

    @Override
    public void run() {
        synchronized (mutedPlayers) {
            PluginUtils.debug("Updating mute information...");
            updateCache();
        }
    }
}
