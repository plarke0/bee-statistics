package net.plarke.bee_statistics.registry;

import net.plarke.bee_statistics.data.HiveData;

import net.minecraft.util.math.BlockPos;

import java.util.Collection;
import java.util.Map;
import java.util.HashMap;

public class HiveRegistry {
    private static final Map<BlockPos, HiveData> HIVES = new HashMap<>();

    //TODO Throw an exception if the block at the given pos is not a BeehiveBlockEntity
    public static void add(BlockPos pos, String id, long time) {
        //TODO Get an actual value for initialHoneyLevel
        HIVES.put(pos, new HiveData(id, pos, time, 0));
    }

    public static void removeById(String id) {
        HIVES.values().removeIf(h -> h.id.equals(id));
    }

    public static Collection<HiveData> getAll() {
        return HIVES.values();
    }

    public static HiveData get(BlockPos pos) {
        return HIVES.get(pos);
    }
}
