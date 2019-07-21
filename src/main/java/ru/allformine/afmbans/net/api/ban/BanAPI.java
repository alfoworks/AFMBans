package ru.allformine.afmbans.net.api.ban;

import com.google.gson.JsonObject;
import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.entity.living.player.Player;
import ru.allformine.afmbans.net.JsonRequest;

import java.net.InetAddress;
import java.net.URL;
import java.time.Duration;

public class BanAPI {
    private String nickname;

    public BanAPI(Player player){
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
        json.addProperty("nickname", this.nickname);
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

    public JsonObject punish(String reason, CommandSource commandSource, Type type,
                             Duration duration, InetAddress address) throws Exception {
        JsonObject json = new JsonObject();
        json.addProperty("source", commandSource.getName());
        json.addProperty("target", this.nickname.toLowerCase());
        json.addProperty("type", type.name());
        json.addProperty("reason", reason);
        if(duration != null){
            json.addProperty("duration", duration.getSeconds());
        }
        return makeRequest("punish", json);
    }

    public JsonObject amnesty(String reason, CommandSource commandSource, Type type) throws Exception {
        JsonObject json = new JsonObject();
        json.addProperty("source", commandSource.getName());
        json.addProperty("target", this.nickname.toLowerCase());
        json.addProperty("type", "un" + type.name());
        json.addProperty("reason", reason);
        return makeRequest("amnesty", json);
    }



}
