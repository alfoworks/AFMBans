package ru.allformine.afmbans.net.api.ban.response.object;

public class Punish {
    public String source;
    public String reason;

    public Punish(String _reason_0) {
        String[] _reason_1 = _reason_0.split(":", 2);
        this.source = _reason_1[0];
        this.reason = _reason_1[1];
    }
}
