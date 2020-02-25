package ru.allformine.afmbans.net.api.ban.response;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import ru.allformine.afmbans.net.api.ban.error.ApiException;
import ru.allformine.afmbans.net.api.ban.response.object.HistoryRecord;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

public class HistoryResponse {
    public List<HistoryRecord> items;
    public boolean ok;
    public ApiException error;

    public HistoryResponse(JsonObject object) throws ParseException {
        JsonArray items = object.getAsJsonArray("items");
        this.items = new ArrayList<>();
        for (JsonElement element : items) {
            HistoryRecord record = new HistoryRecord((JsonObject) element);
            this.items.add(record);
        }
    }
}
