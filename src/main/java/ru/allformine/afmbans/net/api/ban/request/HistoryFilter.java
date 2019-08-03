package ru.allformine.afmbans.net.api.ban.request;

import com.google.gson.JsonObject;

import java.time.Duration;
import java.util.Arrays;
import java.util.List;

public class HistoryFilter {
    private static String[] typesarray = new String[]{"warn", "mute", "ban", "unwarn" , "unmute", "unban", "kick",
                                                      "unwarnall"};
    private static List<String> types = Arrays.asList(typesarray);
    private String target;
    private String source;
    private String reason;
    private Integer type;
    private Long duration;
    private Boolean use_ip;

    public void setTarget(String nickname){
        this.target = nickname;
    }

    public void setSource(String nickname){
        this.source = nickname;
    }

    public void setReason(String reason){
        this.reason = reason;
    }

    public void setType(String type){
        this.type = types.indexOf(type);
    }

    public void setDuration(Duration duration){
        this.duration = duration.getSeconds();
    }

    public void setIpUse(boolean use){
        this.use_ip = use;
    }

    public JsonObject getJson(){
        JsonObject json = new JsonObject();
        if(this.target != null){
            json.addProperty("target", this.target);
        }
        if(this.source != null){
            json.addProperty("source", this.source);
        }
        if(this.reason != null){
            json.addProperty("reason", this.reason);
        }
        if(this.type != null){
            json.addProperty("type", this.type);
        }
        if(this.duration != null){
            json.addProperty("duration", this.duration);
        }
        if(this.use_ip != null){
            json.addProperty("use_ip", this.use_ip);
        }
        return json;
    }

}
