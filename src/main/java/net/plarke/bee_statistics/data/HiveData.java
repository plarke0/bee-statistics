package net.plarke.bee_statistics.data;

import net.minecraft.registry.RegistryKey;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import java.util.ArrayList;
import java.util.List;

public class HiveData {

    public int lastHoneyLevel;
    public String id;
    public BlockPos pos;
    public RegistryKey<World> worldKey;
    public long timeSinceDeposit;
    public List<HiveLog> logs = new ArrayList<>();

    public HiveData(String id, BlockPos pos, RegistryKey<World> worldKey, int initialHoneyLevel) {
        this.id = id;
        this.pos = pos;
        this.worldKey = worldKey;
        this.lastHoneyLevel = initialHoneyLevel;
    }
}

