package ru.allformine.afmbans;

import org.spongepowered.api.entity.living.player.Player;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class MuteCache {
    public static final List<Player> mutedPlayers = Collections.synchronizedList(new ArrayList<>());

    public static boolean playerMuted(Player player) {
        boolean muted;

        synchronized (mutedPlayers) {
            muted = mutedPlayers.contains(player);
        }

        return muted;
    }
}
