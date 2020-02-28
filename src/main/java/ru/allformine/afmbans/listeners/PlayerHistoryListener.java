package ru.allformine.afmbans.listeners;

import org.spongepowered.api.event.Listener;
import org.spongepowered.api.event.network.ClientConnectionEvent;
import ru.allformine.afmbans.AFMBans;
import ru.allformine.afmbans.PluginStatics;
import ru.allformine.afmbans.PluginUtils;
import ru.allformine.afmbans.net.api.ban.BanAPI;
import ru.allformine.afmbans.net.api.ban.error.ApiException;
import ru.allformine.afmbans.net.api.ban.response.IpHistoryResponse;
import ru.allformine.afmbans.net.api.ban.response.object.IpHistoryRecord;

import java.util.stream.Collectors;

public class PlayerHistoryListener {
    @Listener
    public void onPlayerJoin(ClientConnectionEvent.Join event) {
        try {
            BanAPI.addIpToHistory(event.getTargetEntity().getName(), event.getTargetEntity().getConnection().getAddress().getAddress());
        } catch (Exception e) {
            AFMBans.logger.error("Error adding player IP to history:");
            e.printStackTrace();
        }

        IpHistoryResponse response;

        try {
            response = BanAPI.getIpHistory(null, event.getTargetEntity().getConnection().getAddress().getAddress());
        } catch (Exception e) {
            e.printStackTrace();
            if (e instanceof ApiException) {
                AFMBans.logger.error("API error checking player IPs: " + ((ApiException) e).getBody().toString());
            } else {
                AFMBans.logger.error("Error checking player IPs");
            }
            return;
        }

        response.items = response.items.stream().filter(item -> !item.nickname.equals(event.getTargetEntity().getName())).collect(Collectors.toList());

        if (response.items.size() == 0) {
            return;
        }

        PluginStatics.getNotifyChannel().send(PluginUtils.getPlayerTwinksMessage(event.getTargetEntity().getName(), response.items.stream().map(IpHistoryRecord::toString).collect(Collectors.toList())));
    }
}
