package net.plarke.bee_statistics.data;

public class HiveLog {
    public long duration;
    public int honeyLevel;
    public int fillAmount;

    public HiveLog(long fillDuration, int honeyLevel, int fillAmount) {
        this.duration = fillDuration;
        this.honeyLevel = honeyLevel;
        this.fillAmount = fillAmount;
    }
}
