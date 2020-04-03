package ru.allformine.afmbans;

import org.spongepowered.api.entity.living.player.Player;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

public class MuteCache {
    public static final Set<Player> mutedPlayers = Collections.synchronizedSet(new HashSet<>());

    public static boolean isPlayerMuted(Player player) {
        boolean muted;

        synchronized (mutedPlayers) {
            muted = mutedPlayers.contains(player);
        }

        return muted;
    }

    public static void setPlayerMuted(Player player, boolean isMuted) {
        synchronized (mutedPlayers) {
            if (isMuted) {
                mutedPlayers.add(player);
            } else {
                mutedPlayers.remove(player);
            }
        }
    }
}
