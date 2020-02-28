package ru.allformine.afmbans.listeners;

import org.spongepowered.api.event.Listener;
import org.spongepowered.api.event.network.ClientConnectionEvent;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.text.channel.MessageChannel;
import ru.allformine.afmbans.PluginPermissions;
import ru.allformine.afmbans.PluginUtils;
import ru.allformine.afmbans.net.api.ban.BanAPI;
import ru.allformine.afmbans.net.api.ban.PunishType;
import ru.allformine.afmbans.net.api.ban.response.CheckResponse;

public class BanEventListener {
    @Listener
    public void onPlayerLogin(ClientConnectionEvent.Login event) {
        BanAPI banApi = new BanAPI(event.getTargetUser().getName());

        CheckResponse banned;

        try {
            banned = banApi.check(PunishType.BAN, event.getConnection().getAddress().getAddress());
        } catch (Exception e) {
            e.printStackTrace();
            return;
        }

        if (!banned.punished) {
            return;
        }

        event.setMessage(PluginUtils.getBanMessageForPlayer(banned.reason.source, banned.reason.reason, banned.end));
        event.setCancelled(true);

        MessageChannel.permission(PluginPermissions.PLAYER_JOIN_NOTIFY).send(Text.of(event.getTargetUser().getName() + " (забанен) попытался войти."));
    }
}
