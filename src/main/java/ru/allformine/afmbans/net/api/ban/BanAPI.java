package ru.allformine.afmbans.net.api.ban;

import com.google.gson.JsonObject;
import org.spongepowered.api.command.CommandSource;
import ru.allformine.afmbans.PluginUtils;
import ru.allformine.afmbans.net.JsonRequest;
import ru.allformine.afmbans.net.api.ban.error.ApiError;
import ru.allformine.afmbans.net.api.ban.request.HistoryFilter;
import ru.allformine.afmbans.net.api.ban.response.CheckResponse;
import ru.allformine.afmbans.net.api.ban.response.HistoryResponse;
import ru.allformine.afmbans.net.api.ban.response.IpHistoryResponse;

import javax.annotation.Nullable;
import java.io.IOException;
import java.net.InetAddress;
import java.net.URL;
import java.text.ParseException;
import ru.allformine.afmbans.time.Duration;

public class BanAPI {
    private String nickname;

    public BanAPI(String nickname) {
        this.nickname = nickname;
    }

    private static JsonObject makeRequest(String method, JsonObject json) throws IOException, ApiError {
        PluginUtils.debug("Sending JSON: \"" + json.toString() + "\" using method \"" + method + "\"");
        JsonRequest req = new JsonRequest(
                new URL(
                        String.format("http://allformine.ru/ban_api/?method=%s", method)
                ),
                json
        );
        JsonObject jsonResponse = req.getResponseJson();
        if(!jsonResponse.get("ok").getAsBoolean()){
            throw new ApiError(jsonResponse);
        }
        return jsonResponse;
    }

    public static JsonObject addIpToHistory(String nickname, InetAddress address) throws IOException, ApiError {
        JsonObject json = new JsonObject();
        json.addProperty("nickname", nickname);
        json.addProperty("ip", address.getHostAddress());
        json.addProperty("type", "add");
        return makeRequest("ip", json);
    }

    public static IpHistoryResponse getIpHistory(@Nullable String nickname, @Nullable InetAddress address) throws IOException, ApiError {
        JsonObject json = new JsonObject();
        JsonObject filter = new JsonObject();
        if(nickname != null) filter.addProperty("nickname__iexact", nickname);
        if(address != null) filter.addProperty("ip_address", address.getHostAddress());
        json.add("filter", filter);
        json.addProperty("type", "get");
        JsonObject resp = makeRequest("ip", json);
        return new IpHistoryResponse(resp);
    }

    public static CheckResponse checkIpBan(InetAddress address) throws IOException, ParseException, ApiError {
        JsonObject json = new JsonObject();
        json.addProperty("ip", address.getHostAddress());
        json.addProperty("type", "check");
        return new CheckResponse(makeRequest("ip", json));
    }

    public static JsonObject unbanIp(InetAddress address) throws IOException, ApiError {
        JsonObject json = new JsonObject();
        json.addProperty("ip", address.getHostAddress());
        json.addProperty("type", "unban");
        return makeRequest("ip", json);
    }

    public static HistoryResponse getHistory(HistoryFilter filter) throws ParseException, IOException, ApiError {
        JsonObject json = new JsonObject();
        json.add("filter", filter.getJson());
        json.addProperty("type", "get");
        JsonObject resp = makeRequest("history", json);
        return new HistoryResponse(resp);
    }

    public CheckResponse check(PunishType type, @Nullable InetAddress address) throws IOException, ParseException, ApiError {
        JsonObject json = new JsonObject();
        json.addProperty("nickname", this.nickname);
        json.addProperty("type", type.name());
        if(type == PunishType.BAN && address != null){
            json.addProperty("ip", address.getHostAddress());
        }
        JsonObject res = makeRequest("check", json);

        return new CheckResponse(res);
    }

    @Deprecated
    public int getWarns() throws IOException, ApiError {
        JsonObject json = new JsonObject();
        json.addProperty("nickname", this.nickname);
        json.addProperty("type", "Warn");
        JsonObject res = makeRequest("check", json);
        return res.get("count").getAsInt();
    }

    public JsonObject punish(CommandSource commandSource, PunishType type, @Nullable String reason,
                             @Nullable Duration duration, @Nullable InetAddress address) throws IOException, ApiError {
        JsonObject json = new JsonObject();
        json.addProperty("source", commandSource.getName());
        json.addProperty("target", this.nickname);
        json.addProperty("type", type.name());
        if(reason != null) json.addProperty("reason", reason);
        if(duration != null) json.addProperty("duration", duration.getSeconds());
        if(address != null){
            json.addProperty("ip", address.getHostAddress());
            json.addProperty("use_ip", true);
        }

        return makeRequest("punish", json);
    }

    public JsonObject amnesty(CommandSource commandSource, PunishType type) throws IOException, ApiError {
        JsonObject json = new JsonObject();
        json.addProperty("source", commandSource.getName());
        json.addProperty("target", this.nickname.toLowerCase());
        json.addProperty("type", "un" + type.name());
        return makeRequest("amnesty", json);
    }
}
