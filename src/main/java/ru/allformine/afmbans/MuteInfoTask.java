package ru.allformine.afmbans;

import org.spongepowered.api.Sponge;
import org.spongepowered.api.entity.living.player.Player;

public class MuteInfoTask implements Runnable {
    public static void updateCache() {
        for (Player player : Sponge.getServer().getOnlinePlayers()) {
            MuteCache.updatePlayerMute(player);
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
