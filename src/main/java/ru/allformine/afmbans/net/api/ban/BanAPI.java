package ru.allformine.afmbans.net.api.ban;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.entity.living.player.Player;
import ru.allformine.afmbans.PluginUtils;
import ru.allformine.afmbans.net.JsonRequest;
import ru.allformine.afmbans.net.api.ban.error.ApiException;
import ru.allformine.afmbans.net.api.ban.request.HistoryFilter;
import ru.allformine.afmbans.net.api.ban.response.CheckResponse;
import ru.allformine.afmbans.net.api.ban.response.HistoryResponse;
import ru.allformine.afmbans.net.api.ban.response.IpHistoryResponse;
import ru.allformine.afmbans.time.Duration;

import javax.annotation.Nullable;
import java.io.IOException;
import java.net.InetAddress;
import java.net.URL;
import java.text.ParseException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@SuppressWarnings({"UnusedReturnValue", "unused"})
public class BanAPI {
    private String nickname;

    public BanAPI(String nickname) {
        this.nickname = nickname;
    }

    public BanAPI(Player player) {
        this.nickname = player.getName();
    }

    private static JsonObject makeRequest(String method, JsonObject json) throws IOException, ApiException {
        PluginUtils.debug("Sending JSON: \"" + json.toString() + "\" using method \"" + method + "\"");
        JsonRequest req = new JsonRequest(
                new URL(
                        String.format("http://allformine.ru/ban_api/?method=%s", method)
                ),
                json
        );
        JsonObject jsonResponse = req.getResponseJson();
        if (!jsonResponse.get("ok").getAsBoolean()) {
            throw new ApiException(jsonResponse);
        }
        return jsonResponse;
    }

    public static JsonObject addIpToHistory(String nickname, InetAddress address) throws IOException, ApiException {
        JsonObject json = new JsonObject();
        json.addProperty("nickname", nickname);
        json.addProperty("ip", address.getHostAddress());
        json.addProperty("type", "add");
        return makeRequest("ip", json);
    }

    public static IpHistoryResponse getIpHistory(@Nullable String nickname, @Nullable InetAddress address) throws IOException, ApiException {
        JsonObject json = new JsonObject();
        JsonObject filter = new JsonObject();
        if (nickname != null) filter.addProperty("nickname__iexact", nickname);
        if (address != null) filter.addProperty("ip_address", address.getHostAddress());
        json.add("filter", filter);
        json.addProperty("type", "get");
        JsonObject resp = makeRequest("ip", json);
        return new IpHistoryResponse(resp);
    }

    public static CheckResponse checkIpBan(InetAddress address) throws IOException, ParseException, ApiException {
        JsonObject json = new JsonObject();
        json.addProperty("ip", address.getHostAddress());
        json.addProperty("type", "check");
        return new CheckResponse(makeRequest("ip", json));
    }

    public static JsonObject unbanIp(InetAddress address) throws IOException, ApiException {
        JsonObject json = new JsonObject();
        json.addProperty("ip", address.getHostAddress());
        json.addProperty("type", "unban");
        return makeRequest("ip", json);
    }

    public static HistoryResponse getHistory(HistoryFilter filter) throws ParseException, IOException, ApiException {
        JsonObject json = new JsonObject();
        json.add("filter", filter.getJson());
        json.addProperty("type", "get");
        JsonObject resp = makeRequest("history", json);
        return new HistoryResponse(resp);
    }

    public CheckResponse check(PunishType type, @Nullable InetAddress address) throws IOException, ParseException, ApiException {
        JsonObject json = new JsonObject();
        json.addProperty("nickname", this.nickname);
        json.addProperty("type", type.name());
        if (type == PunishType.BAN && address != null) {
            json.addProperty("ip", address.getHostAddress());
        }

        return new CheckResponse(makeRequest("check", json));
    }

    @Deprecated
    public int getWarns() throws IOException, ApiException {
        JsonObject json = new JsonObject();
        json.addProperty("nickname", this.nickname);
        json.addProperty("type", "Warn");
        JsonObject res = makeRequest("check", json);
        return res.get("count").getAsInt();
    }

    public JsonObject punish(CommandSource commandSource, PunishType type, @Nullable String reason,
                             @Nullable Duration duration, @Nullable InetAddress address) throws IOException, ApiException {
        JsonObject json = new JsonObject();
        json.addProperty("source", commandSource.getName());
        json.addProperty("target", this.nickname);
        json.addProperty("type", type.name());
        if (reason != null) json.addProperty("reason", reason);
        if (duration != null) json.addProperty("duration", duration.getSeconds());
        if (address != null) {
            json.addProperty("ip", address.getHostAddress());
            json.addProperty("use_ip", true);
        }

        return makeRequest("punish", json);
    }

    public JsonObject amnesty(CommandSource commandSource, PunishType type) throws IOException, ApiException {
        JsonObject json = new JsonObject();
        json.addProperty("source", commandSource.getName());
        json.addProperty("target", this.nickname.toLowerCase());
        json.addProperty("type", "un" + type.name());
        return makeRequest("amnesty", json);
    }

    public static Map<String, Boolean> massBanCheck(List<String> nicknames) throws IOException, ApiException {
        JsonObject json = new JsonObject();
        JsonArray players = new JsonArray();
        for (String nickname : nicknames) players.add(nickname);
        json.add("players", players);
        JsonObject jsonResponse = makeRequest("massbancheck", json);
//        Type listType = new TypeToken<List<Boolean>>() {
//        }.getType();
//        //List<Boolean> bannedList = new Gson().fromJson(jsonResponse.get("items").getAsJsonArray(), listType);
        JsonArray bannedList = jsonResponse.get("items").getAsJsonArray();
        HashMap<String, Boolean> response = new HashMap<>();
        for (int i = 0, nicknamesSize = nicknames.size(); i < nicknamesSize; i++) {
            String nickname = nicknames.get(i);
            Boolean banned = bannedList.get(i).getAsBoolean();
            response.put(nickname, banned);
        }
        return response;
    }
}
