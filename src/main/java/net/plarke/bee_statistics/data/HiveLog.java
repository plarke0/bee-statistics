package net.plarke.bee_statistics.data;

public class HiveLog {
    public long timestamp;
    public long duration;
    public int honeyLevel;
    public int fillAmount;

    public HiveLog(long timestamp, long fillDuration, int honeyLevel, int fillAmount) {
        this.timestamp = timestamp;
        this.duration = fillDuration;
        this.honeyLevel = honeyLevel;
        this.fillAmount = fillAmount;
    }
}
