package ru.allformine.afmbans.net.api.ban;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class CheckResponse {
    public boolean ok;
    public boolean punished;
    public List<String> reason;
    public int count;
    CheckResponse(JsonObject response){
        this.ok = response.get("ok").getAsBoolean();
        if(this.ok){
            this.punished = response.get("punished").getAsBoolean();
            if(response.has("reason")){
                JsonElement reason = response.get("reason");
                if(reason.isJsonArray()){
                    Type listType = new TypeToken<List<String>>() {}.getType();
                    this.reason = new Gson().fromJson(reason, listType);
                }else{
                    this.reason = new ArrayList<>();
                    this.reason.add(reason.getAsString());
                }
            }
            this.count = response.has("count")?response.get("count").getAsInt():0;
        }
    }
}
