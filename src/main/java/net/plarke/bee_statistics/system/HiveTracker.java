package net.plarke.bee_statistics.system;

import net.minecraft.world.World;
import net.plarke.bee_statistics.registry.HiveRegistry;
import net.plarke.bee_statistics.data.HiveLog;
import net.plarke.bee_statistics.data.HiveData;

import net.minecraft.server.MinecraftServer;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BeehiveBlockEntity;
import net.minecraft.block.BeehiveBlock;

public class HiveTracker {

    public static Boolean trackingActive = false;

    public static void tick(MinecraftServer server) {
        if (!trackingActive) {
            return;
        }
        //TODO Add Arbitrary dimension support
        ServerWorld overworld = server.getWorld(World.OVERWORLD);
        ServerWorld nether = server.getWorld(World.NETHER);
        ServerWorld end = server.getWorld(World.END);

        for (HiveData hive : HiveRegistry.getAll()) {
            ServerWorld world = null;
            if (hive.worldKey.equals(World.OVERWORLD)) {
                world = overworld;
            } else if (hive.worldKey.equals(World.NETHER)) {
                world = nether;
            } else if (hive.worldKey.equals(World.END)) {
                world = end;
            }

            if (world == null) {
                continue;
            }
            BlockEntity entity = world.getBlockEntity(hive.pos);

            if (entity instanceof BeehiveBlockEntity beehive) {
                int honeyLevel = beehive.getCachedState().get(BeehiveBlock.HONEY_LEVEL);
                hive.timeSinceDeposit += 1;

                if (honeyLevel > hive.lastHoneyLevel) {
                    int fillAmount = honeyLevel - hive.lastHoneyLevel;

                    hive.logs.add(new HiveLog(hive.timeSinceDeposit, honeyLevel, fillAmount));

                    hive.timeSinceDeposit = 0;
                    hive.lastHoneyLevel = honeyLevel;
                }

                if (honeyLevel >= 5) {
                    hive.lastHoneyLevel = 0;
                    world.setBlockState(hive.pos, beehive.getCachedState().with(BeehiveBlock.HONEY_LEVEL, 0));
                }
            }
        }
    }
}
