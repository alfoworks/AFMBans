package ru.allformine.afmbans.net.api.ban;

import com.google.gson.JsonObject;
import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.entity.living.player.Player;
import ru.allformine.afmbans.net.JsonRequest;

import java.net.URL;
import java.util.UUID;

public class BanAPI {
    private UUID uuid;
    private String nickname;
    public BanAPI(Player player){
        this.uuid = player.getUniqueId();
        this.nickname = player.getName();
    }


    private static JsonObject makeRequest(String method, JsonObject json) throws Exception {
        JsonRequest req = new JsonRequest(
                new URL(
                        String.format("https://localhost/ban_api?method=%s", method)
                ),
                json
        );
        return req.getResponseJson();
    }

    public enum Type{
        Ban,
        Mute,
        Warn
    }

    public boolean check(Type type) throws Exception {
        JsonObject json = new JsonObject();
        json.addProperty("UUID", this.uuid.toString());
        json.addProperty("type", type.name());
        JsonObject res = makeRequest("check", json);
        return res.get("punished").getAsBoolean();
    }

    public int getWarns() throws Exception {
        JsonObject json = new JsonObject();
        json.addProperty("UUID", this.uuid.toString());
        json.addProperty("type", "Warn");
        JsonObject res = makeRequest("check", json);
        return res.get("count").getAsInt();
    }

    public int warn(String reason, CommandSource source) throws Exception {
        JsonObject json = new JsonObject();
        json.addProperty("target_UUID", this.uuid.toString());
        json.addProperty();
        json.addProperty("type", "warn");
        json.addProperty("reason", reason);
        JsonObject res = makeRequest("punish", json);
        return res.get("count").getAsInt();
    }

}
