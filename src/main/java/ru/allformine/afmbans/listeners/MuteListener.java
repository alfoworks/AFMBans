package ru.allformine.afmbans.listeners;

import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.event.Listener;
import org.spongepowered.api.event.Order;
import org.spongepowered.api.event.command.SendCommandEvent;
import org.spongepowered.api.event.filter.Getter;
import org.spongepowered.api.event.filter.cause.First;
import org.spongepowered.api.event.message.MessageChannelEvent;
import org.spongepowered.api.event.network.ClientConnectionEvent;
import ru.allformine.afmbans.MuteCache;
import ru.allformine.afmbans.PluginStatics;
import ru.allformine.afmbans.PluginUtils;

public class MuteListener {
    @Listener(order = Order.FIRST)
    public void onMessage(MessageChannelEvent.Chat event, @First Player player) {
        if (MuteCache.isPlayerMuted(player)) {
            event.setCancelled(true);
            PluginUtils.sendMuteMessage(player);
        }
    }

    @Listener(order = Order.FIRST)
    public void onCommand(SendCommandEvent event, @First Player player) {
        if (PluginStatics.prohibitedMuteCommands.contains(event.getCommand().toLowerCase())) {
            event.setCancelled(true);
            PluginUtils.sendMuteMessage(player);
        }
    }

    @Listener
    public void onJoin(ClientConnectionEvent.Join event, @Getter("getTargetEntity") Player player) {
        MuteCache.updatePlayerMute(player);
    }
}
