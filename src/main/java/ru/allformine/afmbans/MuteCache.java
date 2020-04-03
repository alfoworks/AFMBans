package ru.allformine.afmbans;

import org.spongepowered.api.entity.living.player.Player;

import javax.annotation.Nullable;
import java.util.*;

public class MuteCache {
    public static final Set<Player> mutedPlayers = Collections.synchronizedSet(new HashSet<>());
    public static final Map<Player, Date> mutedPlayersDates = Collections.synchronizedMap(new HashMap<>());

    public static boolean isPlayerMuted(Player player) {
        boolean muted;

        synchronized (mutedPlayers) {
            muted = mutedPlayers.contains(player);
        }

        return muted;
    }

    public static Date getPlayerMuteDeadline(Player player) {
        return mutedPlayersDates.get(player);
    }

    public static void setPlayerMuted(Player player, boolean isMuted, @Nullable Date date) {
        synchronized (mutedPlayers) {
            if (isMuted) {
                mutedPlayers.add(player);
                if (date != null) mutedPlayersDates.put(player, date);
            } else {
                mutedPlayers.remove(player);
                mutedPlayersDates.remove(player);
            }
        }
    }
}