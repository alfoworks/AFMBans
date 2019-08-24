package ru.allformine.afmbans.net.api.ban.error;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

@SuppressWarnings("unused")
public class ApiError extends Exception {
    private int errorCode;
    private String description;
    private JsonObject body;

    public ApiError(JsonObject object){
        if(!object.get("ok").getAsBoolean()){
            this.errorCode = object.get("error_code").getAsInt();
            this.description = object.get("error").getAsString();
            this.body = object;
        }
    }

    public int getErrorCode(){
        return this.errorCode;
    }

    public String getDescription(){
        return this.description;
    }

    public JsonElement getBody(){
        return this.body;
    }

}
