package ru.allformine.afmbans;

import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.scheduler.Task;
import ru.allformine.afmbans.net.api.ban.BanAPI;
import ru.allformine.afmbans.net.api.ban.PunishType;
import ru.allformine.afmbans.net.api.ban.error.ApiException;
import ru.allformine.afmbans.net.api.ban.response.CheckResponse;

import javax.annotation.Nullable;
import java.io.IOException;
import java.text.ParseException;
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

    public static void updatePlayerMute(Player player) {
        Task.builder().execute(() -> {
            synchronized (mutedPlayers) {
                BanAPI api = new BanAPI(player.getName());

                try {
                    CheckResponse response = api.check(PunishType.MUTE, null);

                    if (response.punished) {
                        mutedPlayers.add(player);

                        if (response.end != null) MuteCache.mutedPlayersDates.put(player, response.end);
                    }
                } catch (IOException | ParseException | ApiException e) {
                    AFMBans.logger.error("Error checking player for mute:");
                    e.printStackTrace();
                }
            }
        }).async().submit(AFMBans.instance);
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