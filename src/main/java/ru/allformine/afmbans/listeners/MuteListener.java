package ru.allformine.afmbans.listeners;

import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.event.Listener;
import org.spongepowered.api.event.Order;
import org.spongepowered.api.event.command.SendCommandEvent;
import org.spongepowered.api.event.filter.cause.First;
import org.spongepowered.api.event.message.MessageChannelEvent;
import ru.allformine.afmbans.MuteCache;
import ru.allformine.afmbans.PluginUtils;

import java.util.Arrays;
import java.util.List;

public class MuteListener {
    public static List<String> prohibitedCommands = Arrays.asList("g", "t", "l", "msg", "tell", "bc"); //FIXME дополни меня

    @Listener(order = Order.FIRST)
    public void onMessage(MessageChannelEvent.Chat event, @First Player player) {
        if (MuteCache.playerMuted(player)) {
            event.setCancelled(true);
            PluginUtils.sendMuteMessage(player);
        }
    }

    @Listener(order = Order.FIRST)
    public void onCommand(SendCommandEvent event, @First Player player) {
        if (prohibitedCommands.contains(event.getCommand().toLowerCase())) {
            event.setCancelled(true);
            PluginUtils.sendMuteMessage(player);
        }
    }
}
