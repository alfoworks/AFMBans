package ru.allformine.afmbans.net.api.ban;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import java.util.ArrayList;
import java.util.List;

public class BasicResponse {
    List<JsonObject> objects;
    private boolean hasError = false;
    private BasicError error;
    public BasicResponse(JsonObject object){
        if(object.get("ok").getAsBoolean()) {
            this.objects = new ArrayList<>();
            for (JsonElement elem : object.getAsJsonArray("items")) {
                this.objects.add((JsonObject) elem);
            }
        }else{
            this.hasError = true;
            this.error = new BasicError(object);
        }
    }

    public boolean isHasError(){
        return hasError;
    }

    public BasicError getError(){
        return this.error;
    }

}
