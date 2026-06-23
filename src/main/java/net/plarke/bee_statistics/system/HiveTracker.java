package net.plarke.bee_statistics.system;

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
        ServerWorld world = server.getOverworld();
        long currentTime = world.getTime();

        for (HiveData hive : HiveRegistry.getAll()) {
            BlockEntity entity = world.getBlockEntity(hive.pos);

            if (entity instanceof BeehiveBlockEntity beehive) {
                int honeyLevel = beehive.getCachedState().get(BeehiveBlock.HONEY_LEVEL);

                if (honeyLevel > hive.lastHoneyLevel) {
                    long duration = currentTime - hive.lastDepositTime;
                    int fillAmount = honeyLevel - hive.lastHoneyLevel;

                    hive.logs.add(new HiveLog(currentTime, duration, honeyLevel, fillAmount));

                    hive.lastDepositTime = currentTime;
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
