package ru.allformine.afmbans.listeners;

import com.google.gson.JsonObject;
import org.spongepowered.api.event.Listener;
import org.spongepowered.api.event.network.ClientConnectionEvent;
import org.spongepowered.api.text.channel.MessageChannel;
import ru.allformine.afmbans.AFMBans;
import ru.allformine.afmbans.PluginPermissions;
import ru.allformine.afmbans.PluginUtils;
import ru.allformine.afmbans.net.api.ban.BanAPI;
import ru.allformine.afmbans.net.api.ban.BasicResponse;

import java.util.ArrayList;
import java.util.List;

public class PlayerHistoryListener {
    @Listener
    public void onPlayerJoin(ClientConnectionEvent.Join event) {
        try {
            BanAPI.addIpToHistory(event.getTargetEntity().getName(), event.getTargetEntity().getConnection().getAddress().getAddress());
        } catch (Exception e) {
            AFMBans.logger.error("Error adding player IP to history:");
            e.printStackTrace();
        }

        ArrayList<String> nicks = new ArrayList<>();

        BasicResponse response;

        try {
            response = BanAPI.getIpHistory(event.getTargetEntity().getName(), event.getTargetEntity().getConnection().getAddress().getAddress());
        } catch (Exception e) {
            AFMBans.logger.error("Error checking player IPs");
            e.printStackTrace();
            return;
        }

        if (response.hasError()) {
            AFMBans.logger.error("API error checking player IPs: " + response.getError().getDescription());
            return;
        }

        for (JsonObject object : response.objects) {
            String nick = object.get("nickname").getAsString();

            if (!nick.equals(event.getTargetEntity().getName())) {
                nicks.add(nick);
            }
        }

        if (nicks.size() == 0) {
            return;
        }

        MessageChannel.permission(PluginPermissions.PLAYER_TWINK_NOTIFY).send(PluginUtils.getPlayerTwinksMessage(event.getTargetEntity().getName(), nicks));
    }
}
