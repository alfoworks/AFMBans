package ru.allformine.afmbans.listeners;

import org.spongepowered.api.event.Listener;
import org.spongepowered.api.event.network.ClientConnectionEvent;
import ru.allformine.afmbans.PluginUtils;
import ru.allformine.afmbans.net.api.ban.BanAPI;
import ru.allformine.afmbans.net.api.ban.PunishType;

public class BanEventListener {
    @Listener
    public void onPlayerLogin(ClientConnectionEvent.Login event) {
        BanAPI banApi = new BanAPI(event.getTargetUser().getName());

        boolean banned;

        try {
            banned = banApi.check(PunishType.BAN, event.getConnection().getAddress().getAddress());
        } catch (Exception e) {
            e.printStackTrace();
            return;
        }

        if (!banned) {
            return;
        }

        event.setMessage(PluginUtils.getBanMessageForPlayer("Iterator", "Плохое поведение"));
        event.setCancelled(true);
    }
}
