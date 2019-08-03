package ru.allformine.afmbans.net.api.ban.response;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;
import ru.allformine.afmbans.net.api.ban.error.BasicError;
import ru.allformine.afmbans.net.api.ban.response.object.Punish;

import java.lang.reflect.Type;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class CheckResponse {
    private static DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSXXX");
    public boolean ok;
    public boolean punished;
    public List<Punish> reason;
    public int count;
    public Date start;
    public Date end;
    public BasicError error;
    public CheckResponse(JsonObject response) throws ParseException {
        this.ok = response.get("ok").getAsBoolean();
        if(this.ok){
            this.punished = response.get("punished").getAsBoolean();
            if(response.has("reason")){
                JsonElement jsonReason = response.get("reason");
                if(jsonReason.isJsonArray()){
                    Type listType = new TypeToken<List<String>>() {}.getType();
                    List<String> reasons = new Gson().fromJson(jsonReason, listType);
                    this.reason = new ArrayList<>();
                    reasons.forEach(reason -> this.reason.add(new Punish(reason)));
                }else{
                    this.reason = new ArrayList<>();
                    this.reason.add(new Punish(jsonReason.getAsString()));
                }
            }
            this.count = response.has("count")?response.get("count").getAsInt():0;
            if(response.has("start") && response.has("end")) {
                this.start = dateFormat.parse(response.get("start").getAsString());
                this.end = dateFormat.parse(response.get("end").getAsString());
            }
        }else{
            this.error = new BasicError(response);
        }
    }
}