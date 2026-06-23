package net.plarke.bee_statistics.command;

import net.minecraft.command.argument.BlockPosArgumentType;
import net.minecraft.text.Text;
import net.plarke.bee_statistics.data.HiveData;
import net.plarke.bee_statistics.exceptions.InvalidBlockTypeAtPosition;
import net.plarke.bee_statistics.registry.HiveRegistry;
import net.plarke.bee_statistics.system.HiveTracker;
import net.plarke.bee_statistics.system.JsonExporter;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.StringArgumentType;

import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;

import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.command.CommandManager;
import net.minecraft.util.math.BlockPos;

import java.nio.file.FileAlreadyExistsException;


public class HiveCommand {
    public static void register() {
        CommandRegistrationCallback.EVENT.register(
            (dispatcher, registryAccess, environment) ->
                registerCommands(dispatcher)
        );
    }

    private static String stringifyPos(BlockPos pos) {
        return "(" + pos.getX() + ", " + pos.getY() + ", " + pos.getZ() + ")";
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
                                                String trackMessage = "\"" + id + "\" successfully added to registry";
                                                src.sendMessage(Text.literal(trackMessage));
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
                        // USING ID
                        .then(CommandManager.argument("id", StringArgumentType.word())
                                .executes(ctx -> {
                                    ServerCommandSource src = ctx.getSource();
                                    String id = StringArgumentType.getString(ctx, "id");
                                    if (HiveRegistry.removeById(id)) {
                                        String removeMessage = "\"" + id + "\" successfully removed from registry";
                                        src.sendMessage(Text.literal(removeMessage));
                                        return 1;
                                    } else {
                                        String removeErrorMessage = "\"" + id + "\" was unable to be removed from registry";
                                        src.sendError(Text.literal(removeErrorMessage));
                                        return 0;
                                    }
                                })
                        )
                        // USING POSITION
                        .then(CommandManager.argument("pos", BlockPosArgumentType.blockPos())
                                .executes(ctx -> {
                                    ServerCommandSource src = ctx.getSource();
                                    BlockPos pos = BlockPosArgumentType.getBlockPos(ctx, "pos");
                                    if (HiveRegistry.removeByPos(pos)) {
                                        String removeMessage = "Hive at " + stringifyPos(pos) + " successfully removed from registry";
                                        src.sendMessage(Text.literal(removeMessage));
                                        return 1;
                                    } else {
                                        String removeErrorMessage = "Hive at " + stringifyPos(pos) + " was unable to be removed from registry";
                                        src.sendError(Text.literal(removeErrorMessage));
                                        return 0;
                                    }
                                })
                        )
                )
                // EXPORT
                .then(CommandManager.literal("export")
                        .then(CommandManager.argument("filename", StringArgumentType.word())
                                .executes(ctx -> {
                                    ServerCommandSource src = ctx.getSource();
                                    String fileName = StringArgumentType.getString(ctx, "filename");

                                    try {
                                        JsonExporter.export(fileName);
                                    } catch (FileAlreadyExistsException e) {
                                        src.sendError(Text.literal(e.getMessage()));
                                        return 0;
                                    }

                                    String exportMessage = "Successfully saved data to \"" + fileName + ".json\"";
                                    src.sendMessage(Text.literal(exportMessage));
                                    return 1;
                                })
                        )
                )
                // LIST
                .then(CommandManager.literal("list")
                        .executes(ctx -> {
                            ServerCommandSource src = ctx.getSource();
                            for (HiveData hiveData : HiveRegistry.getAll()) {
                                String hiveEntry = hiveData.id + ": " + stringifyPos(hiveData.pos);
                                src.sendMessage(Text.literal(hiveEntry));
                            }
                            if (HiveRegistry.isEmpty()) {
                                src.sendMessage(Text.literal("No hives are currently being tracked"));
                            }
                            return 0;
                        })
                )
                // TRACKING
                .then(CommandManager.literal("tracking")
                        // START
                        .then(CommandManager.literal("start")
                                .executes(ctx -> {
                                    HiveTracker.trackingActive = true;
                                    ctx.getSource().sendMessage(Text.literal("Starting data collection"));
                                    return 1;
                                })
                        )
                        // STOP
                        .then(CommandManager.literal("stop")
                                .executes(ctx -> {
                                    HiveTracker.trackingActive = false;
                                    ctx.getSource().sendMessage(Text.literal("Stopping data collection"));
                                    return 1;
                                })
                        )
                        // RESET
                        .then(CommandManager.literal("reset")
                                .executes(ctx -> {
                                    return 0;
                                })
                        )
                )
        );
    }
}
