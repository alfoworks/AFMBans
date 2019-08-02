package ru.allformine.afmbans.net.api.ban;

import com.google.gson.JsonObject;

public class CheckResponse {
    public boolean ok;
    public boolean punished;
    public String reason;
    public int count;
    CheckResponse(JsonObject response){
        this.ok = response.get("ok").getAsBoolean();
        if(this.ok){
            this.punished = response.get("punished").getAsBoolean();
            this.reason = response.has("reason")?response.get("reason").getAsString():"Плохое поведение";
            this.count = response.has("count")?response.get("count").getAsInt():0;
        }
    }
}
