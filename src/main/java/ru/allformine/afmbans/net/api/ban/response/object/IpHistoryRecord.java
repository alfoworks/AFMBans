package ru.allformine.afmbans.net.api.ban.response.object;

import com.google.gson.JsonObject;

import java.net.InetAddress;
import java.net.UnknownHostException;

public class IpHistoryRecord {
    public InetAddress ip;
    public String nickname;

    public IpHistoryRecord(JsonObject record) throws UnknownHostException {
        this.nickname = record.get("nickname").getAsString();
        this.ip = InetAddress.getByName(record.get("ip").getAsString());
    }

    public String toString(){
        return this.nickname + "@" + this.ip.getHostAddress();
    }
}
