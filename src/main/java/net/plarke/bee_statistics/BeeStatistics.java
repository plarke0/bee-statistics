package net.plarke.bee_statistics;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents;

import net.plarke.bee_statistics.system.HiveTracker;
import net.plarke.bee_statistics.command.HiveCommand;


import net.minecraft.util.Identifier;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class BeeStatistics implements ModInitializer {
	public static final String MOD_ID = "bee-statistics";
	public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

	@Override
	public void onInitialize() {
		// Register tick logic
		ServerTickEvents.END_SERVER_TICK.register(HiveTracker::tick);

		// Register commands
		HiveCommand.register();
	}

	public static Identifier id(String path) {
		return Identifier.of(MOD_ID, path);
	}
}
