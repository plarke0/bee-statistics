package net.plarke.bee_statistics.registry;

import net.minecraft.block.BeehiveBlock;
import net.minecraft.block.entity.BeehiveBlockEntity;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.registry.RegistryKey;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.world.World;
import net.plarke.bee_statistics.data.HiveData;

import net.minecraft.util.math.BlockPos;
import net.plarke.bee_statistics.exceptions.InvalidBlockTypeAtPosition;

import java.util.Collection;
import java.util.Map;
import java.util.HashMap;

public class HiveRegistry {
    //TODO Add restrictions to duplicate id's and duplicate positions
    private static final Map<BlockPos, HiveData> HIVES = new HashMap<>();

    public static void add(BlockPos pos, String id, ServerWorld world) throws InvalidBlockTypeAtPosition {
        BlockEntity entity = world.getBlockEntity(pos);

        if (!(entity instanceof BeehiveBlockEntity beehive)) {
            throw new InvalidBlockTypeAtPosition("Block type must be either a bee hive or a bee nest");
        }

        int honeyLevel = beehive.getCachedState().get(BeehiveBlock.HONEY_LEVEL);
        RegistryKey<World> worldKey = world.getRegistryKey();
        HIVES.put(pos, new HiveData(id, pos, worldKey, honeyLevel));
    }

    public static Boolean removeById(String id) {
        return HIVES.values().removeIf(h -> h.id.equals(id));
    }

    public static Boolean removeByPos(BlockPos pos) {
        return HIVES.values().removeIf(h -> h.pos.equals(pos));
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
