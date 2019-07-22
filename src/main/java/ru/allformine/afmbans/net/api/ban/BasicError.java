package ru.allformine.afmbans.net.api.ban;

import com.google.gson.JsonObject;
import java.lang.String;

public class BasicError {
    private int errorCode;
    private String description;
    public BasicError(JsonObject object){
        if(!object.get("ok").getAsBoolean()){
            this.errorCode = object.get("error_code").getAsInt();
            this.description = object.get("error").getAsString();
        }
    }

    public int getErrorCode(){
        return this.errorCode;
    }

    public String getDescription(){
        return this.description;
    }

}
