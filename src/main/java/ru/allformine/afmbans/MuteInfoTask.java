package ru.allformine.afmbans;

import org.spongepowered.api.Sponge;
import org.spongepowered.api.entity.living.player.Player;
import ru.allformine.afmbans.net.api.ban.BanAPI;
import ru.allformine.afmbans.net.api.ban.PunishType;
import ru.allformine.afmbans.net.api.ban.error.ApiException;
import ru.allformine.afmbans.net.api.ban.response.CheckResponse;

import java.io.IOException;
import java.text.ParseException;

public class MuteInfoTask implements Runnable {
    public static void updateCache() {
        synchronized (MuteCache.mutedPlayers) {
            MuteCache.mutedPlayers.clear();

            for (Player player : Sponge.getServer().getOnlinePlayers()) {
                BanAPI api = new BanAPI(player.getName());
                try {
                    CheckResponse response = api.check(PunishType.MUTE, null);

                    if (response.punished) {
                        MuteCache.mutedPlayers.add(player);

                        if (response.end != null) MuteCache.mutedPlayersDates.put(player, response.end);
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
        synchronized (MuteCache.mutedPlayers) {
            PluginUtils.debug("Updating mute information...");
            updateCache();
        }
    }
}
