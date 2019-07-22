package ru.allformine.afmbans.net.api.ban;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class BasicResponse {
    List<JsonObject> objects;
    public BasicResponse(JsonObject object){
        this.objects = new ArrayList<>();
        for(JsonElement elem : object.getAsJsonArray("items")) {
            this.objects.add((JsonObject) elem);
        }
    }
}
