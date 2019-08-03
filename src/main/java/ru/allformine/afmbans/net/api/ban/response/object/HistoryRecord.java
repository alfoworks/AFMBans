package ru.allformine.afmbans.net.api.ban.response.object;

import com.google.gson.JsonObject;
import ru.allformine.afmbans.PluginStatics;


import java.math.BigInteger;
import java.text.ParseException;
import java.util.Date;

public class HistoryRecord {
    public String target;
    public String source;
    public String reason;
    public int type;
    public Date start_date;
    public BigInteger duration;
    public boolean use_ip;

    public HistoryRecord(JsonObject record) throws ParseException {
        this.target = record.get("target").getAsString();
        this.source = record.get("source").getAsString();
        this.reason = record.get("reason").getAsString();
        this.type = record.get("type").getAsInt();
        this.start_date = PluginStatics.dateFormat.parse(record.get("start_date").getAsString());
        this.duration = record.get("duration").getAsBigInteger();
        this.use_ip = record.get("use_ip").getAsBoolean();
    }
}
