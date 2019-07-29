package ru.allformine.afmbans.net.api.ban;

import com.google.gson.JsonObject;
import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.entity.living.player.Player;
import ru.allformine.afmbans.net.JsonRequest;

import javax.annotation.Nullable;
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
                        String.format("http://127.0.0.1/ban_api?method=%s", method)
                ),
                json
        );
        return req.getResponseJson();
    }

    public enum Type{
        Ban,
        Mute,
        Warn,
        Kick
    }

    public boolean check(Type type, @Nullable InetAddress address) throws Exception {
        JsonObject json = new JsonObject();
        json.addProperty("nickname", this.nickname);
        json.addProperty("type", type.name());
        if(type == Type.Ban && address != null){
            json.addProperty("ip", address.getHostAddress());
        }
        JsonObject res = makeRequest("check", json);
        return res.get("punished").getAsBoolean();
    }

    public int getWarns() throws Exception {
        JsonObject json = new JsonObject();
        json.addProperty("nickname", this.nickname);
        json.addProperty("type", "Warn");
        JsonObject res = makeRequest("check", json);
        return res.get("count").getAsInt();
    }

    public JsonObject punish(CommandSource commandSource, Type type, @Nullable String reason,
                             @Nullable Duration duration, @Nullable InetAddress address) throws Exception {
        JsonObject json = new JsonObject();
        json.addProperty("source", commandSource.getName());
        json.addProperty("target", this.nickname.toLowerCase());
        json.addProperty("type", type.name());
        if(reason != null) json.addProperty("reason", reason);
        if(duration != null) json.addProperty("duration", duration.getSeconds());
        if(address != null){
            json.addProperty("ip", address.getHostAddress());
            json.addProperty("use_ip", true);
        }
        return makeRequest("punish", json);
    }

    public JsonObject amnesty(CommandSource commandSource, Type type) throws Exception {
        JsonObject json = new JsonObject();
        json.addProperty("source", commandSource.getName());
        json.addProperty("target", this.nickname.toLowerCase());
        json.addProperty("type", "un" + type.name());
        return makeRequest("amnesty", json);
    }

    public static JsonObject addIpToHistory(String nickname, InetAddress address) throws Exception {
        JsonObject json = new JsonObject();
        json.addProperty("nickname", nickname);
        json.addProperty("ip", address.getHostAddress());
        json.addProperty("type", "add");
        return makeRequest("ip", json);
    }

    public static BasicResponse getIpHistory(@Nullable String nickname, @Nullable InetAddress address) throws Exception {
        // Один из параметров обязателен
        JsonObject json = new JsonObject();
        if(nickname != null) json.addProperty("nickname", nickname);
        if(address != null) json.addProperty("ip", address.getHostAddress());
        json.addProperty("type", "get");
        JsonObject resp = makeRequest("ip", json);
        return new BasicResponse(resp);
    }
}
