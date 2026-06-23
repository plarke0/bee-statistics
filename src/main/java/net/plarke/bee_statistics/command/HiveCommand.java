package net.plarke.bee_statistics.command;

import net.minecraft.command.argument.BlockPosArgumentType;
import net.minecraft.text.Text;
import net.plarke.bee_statistics.data.HiveData;
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
                )
                // EXPORT
                .then(CommandManager.literal("export")
                        //TODO Add command feedback
                        .executes(ctx -> {
                            JsonExporter.export();
                            return 1;
                        })
                )
                // LIST
                .then(CommandManager.literal("list")
                        .executes(ctx -> {
                            ServerCommandSource src = ctx.getSource();
                            for (HiveData hiveData : HiveRegistry.getAll()) {
                                String hiveEntry = hiveData.id + ": " + hiveData.pos.toString();
                                src.sendMessage(Text.literal(hiveEntry));
                            }
                            if (HiveRegistry.isEmpty()) {
                                src.sendMessage(Text.literal("No hives are currently being tracked"));
                            }
                            return 0;
                        })
                )
                //TODO Add a command to stop/start data collection
        );
    }
}
