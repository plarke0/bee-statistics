package net.plarke.bee_statistics.registry;

import net.minecraft.block.entity.BeehiveBlockEntity;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.server.world.ServerWorld;
import net.plarke.bee_statistics.data.HiveData;

import net.minecraft.util.math.BlockPos;
import net.plarke.bee_statistics.exceptions.InvalidBlockTypeAtPosition;

import java.util.Collection;
import java.util.Map;
import java.util.HashMap;

public class HiveRegistry {
    private static final Map<BlockPos, HiveData> HIVES = new HashMap<>();

    public static void add(BlockPos pos, String id, ServerWorld world) throws InvalidBlockTypeAtPosition {
        long time = world.getTime();
        BlockEntity entity = world.getBlockEntity(pos);

        if (!(entity instanceof BeehiveBlockEntity)) {
            throw new InvalidBlockTypeAtPosition("Block type must be either a bee hive or a bee nest");
        }
        //TODO Get an actual value for initialHoneyLevel
        HIVES.put(pos, new HiveData(id, pos, time, 0));
    }

    public static Boolean removeById(String id) {
        return HIVES.values().removeIf(h -> h.id.equals(id));
    }

    public static Collection<HiveData> getAll() {
        return HIVES.values();
    }

    public static Boolean isEmpty() {
        return HIVES.isEmpty();
    }

    public static HiveData get(BlockPos pos) {
        return HIVES.get(pos);
    }
}
