package ru.allformine.afmbans.net.api.ban.response;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import ru.allformine.afmbans.net.api.ban.error.ApiError;
import ru.allformine.afmbans.net.api.ban.response.object.IpHistoryRecord;

import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;

public class IpHistoryResponse {
    public List<IpHistoryRecord> items;
    public ApiError error;

    public IpHistoryResponse(JsonObject object) throws UnknownHostException {
        JsonArray items = object.getAsJsonArray("items");
        this.items = new ArrayList<>();
        for(JsonElement element : items){
            IpHistoryRecord record = new IpHistoryRecord((JsonObject) element);
            this.items.add(record);
        }
    }
}
