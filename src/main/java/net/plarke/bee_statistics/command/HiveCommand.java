package net.plarke.bee_statistics.command;

import net.minecraft.command.argument.BlockPosArgumentType;
import net.minecraft.text.Text;
import net.plarke.bee_statistics.exceptions.InvalidBlockTypeAtPosition;
import net.plarke.bee_statistics.registry.HiveRegistry;
import net.plarke.bee_statistics.system.JsonExporter;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.StringArgumentType;

import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;

import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.command.CommandManager;
import net.minecraft.util.math.BlockPos;


public class HiveCommand {
    public static void register() {
        CommandRegistrationCallback.EVENT.register(
            (dispatcher, registryAccess, environment) ->
                registerCommands(dispatcher)
        );
    }

    private static void registerCommands(CommandDispatcher<ServerCommandSource> dispatcher) {
        dispatcher.register(CommandManager.literal("beehive")
                // TRACK
                .then(CommandManager.literal("track")
                        .then(CommandManager.argument("id", StringArgumentType.word())
                                .then(CommandManager.argument("pos", BlockPosArgumentType.blockPos())
                                        .executes(ctx -> {
                                            ServerCommandSource src = ctx.getSource();
                                            BlockPos pos = BlockPosArgumentType.getBlockPos(ctx, "pos");
                                            String id = StringArgumentType.getString(ctx, "id");
                                            try {
                                                HiveRegistry.add(pos, id, src.getWorld());
                                            } catch (InvalidBlockTypeAtPosition e) {
                                                src.sendError(Text.literal("Tracked blocks must be bee hives or bee nests"));
                                                return 0;
                                            }
                                            return 1;
                                        })
                                )
                        )
                )
                // UNTRACK
                .then(CommandManager.literal("untrack")
                        .then(CommandManager.argument("id", StringArgumentType.word())
                                .executes(ctx -> {
                                    String id = StringArgumentType.getString(ctx, "id");
                                    HiveRegistry.removeById(id);
                                    return 1;
                                })
                        )
                )
                // EXPORT
                .then(CommandManager.literal("export")
                        .executes(ctx -> {
                            JsonExporter.export();
                            return 1;
                        })
                )
                //TODO Add a list command to view tracked hives (<id>: <pos>)
                //TODO Add a command to stop/start data collection
        );
    }
}
