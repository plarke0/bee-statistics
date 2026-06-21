package net.plarke.bee_statistics.data;

import net.minecraft.util.math.BlockPos;

import java.util.ArrayList;
import java.util.List;

public class HiveData {

    public int lastHoneyLevel = 0;

    public String id;
    public BlockPos pos;
    public long lastDepositTime;
    public List<HiveLog> logs = new ArrayList<>();

    public HiveData(String id, BlockPos pos, long startTime, int initialHoneyLevel) {
        this.id = id;
        this.pos = pos;
        this.lastDepositTime = startTime;
        this.lastHoneyLevel = initialHoneyLevel;
    }
}

