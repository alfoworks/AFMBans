package ru.allformine.afmbans.net.api.ban.response;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import ru.allformine.afmbans.PluginStatics;
import ru.allformine.afmbans.net.api.ban.response.object.Punish;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@SuppressWarnings("WeakerAccess")
public class CheckResponse {
    public boolean punished;
    public List<Punish> reasonList;
    public Punish reason;
    public int count;
    public Date start;
    public Date end;
    public String target;

    public CheckResponse(JsonObject response) throws ParseException {
        this.punished = response.get("punished").getAsBoolean();
        this.target = response.get("target").getAsString();
        if(this.punished) {
            if (response.has("reason")) {
                JsonElement jsonReason = response.get("reason");
                if (jsonReason.isJsonArray()) {
                    this.reasonList = new ArrayList<>();
                    for (JsonElement s : ((JsonArray) jsonReason)) this.reasonList.add(new Punish(s.getAsString()));
                } else {
                    this.reason = new Punish(jsonReason.getAsString());
                }
            }
            this.count = response.has("count") ? response.get("count").getAsInt() : 0;
            this.start = PluginStatics.dateFormat.parse(response.get("start").getAsString());
            if (response.has("end")) this.end = PluginStatics.dateFormat.parse(response.get("end").getAsString());
        }
    }
}
