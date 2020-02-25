package ru.allformine.afmbans.time;

@SuppressWarnings({"WeakerAccess", "unused"})
public class Duration {
    public static Duration ZERO = new Duration(0);
    private long seconds;

    private Duration(long seconds) {
        this.seconds = seconds;
    }

    public long getSeconds() {
        return seconds;
    }

    public static Duration ofSeconds(long seconds) {
        return new Duration(seconds);
    }

    public static Duration ofMinutes(long minutes) {
        return ofSeconds(minutes * 60);
    }

    public static Duration ofHours(long hours) {
        return ofSeconds(hours * 3600);
    }

    public static Duration ofDays(long days) {
        return ofSeconds(days * 86400);
    }

    public static Duration ofWeeks(long weeks) {
        return ofSeconds(weeks * 604800L);
    }

    public static Duration ofMonths(long months) {
        return ofSeconds(months * 2592000);
    }

    public static Duration ofYears(long years) {
        return ofSeconds(years * 31536000);
    }
}
