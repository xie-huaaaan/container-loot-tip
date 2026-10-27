package dev.containerloottip.client;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.context.CommandContext;
import dev.containerloottip.ContainerLootTip;
import dev.containerloottip.config.ClientSettings;
import dev.containerloottip.config.DetailLevel;
import dev.containerloottip.config.PanelMode;
import dev.containerloottip.config.PanelSide;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.network.chat.Component;

import java.util.concurrent.CompletableFuture;

/** Client-side commands for changing the JSON config without a settings screen. */
public final class ClientCommand {
    private ClientCommand() {
    }

    public static void register(CommandDispatcher<CommandSourceStack> dispatcher) {
        dispatcher.register(Commands.literal(ContainerLootTip.MOD_ID)
                .then(Commands.literal("mode")
                        .then(Commands.argument("value", StringArgumentType.word())
                                .suggests((source, builder) -> {
                                    for (PanelMode mode : PanelMode.values()) {
                                        builder.suggest(mode.name());
                                    }

                                    return CompletableFuture.completedFuture(builder.build());
                                })
                                .executes(ClientCommand::setMode)))
                .then(Commands.literal("side")
                        .then(Commands.argument("value", StringArgumentType.word())
                                .suggests((source, builder) -> {
                                    for (PanelSide side : PanelSide.values()) {
                                        builder.suggest(side.name());
                                    }

                                    return CompletableFuture.completedFuture(builder.build());
                                })
                                .executes(ClientCommand::setSide)))
                .then(Commands.literal("detail")
                        .then(Commands.argument("value", StringArgumentType.word())
                                .suggests((source, builder) -> {
                                    for (DetailLevel level : DetailLevel.values()) {
                                        builder.suggest(level.name());
                                    }

                                    return CompletableFuture.completedFuture(builder.build());
                                })
                                .executes(ClientCommand::setDetail)))
                .then(Commands.literal("collapse")
                        .then(Commands.argument("value", StringArgumentType.word())
                                .suggests((source, builder) -> {
                                    builder.suggest("true");
                                    builder.suggest("false");
                                    return CompletableFuture.completedFuture(builder.build());
                                })
                                .executes(ClientCommand::setCollapsed))));
    }

    private static int setMode(CommandContext<CommandSourceStack> context) {
        PanelMode mode = PanelMode.parse(StringArgumentType.getString(context, "value"));

        if (mode == null) {
            return invalid(context);
        }

        ClientSettings.get().setPanelMode(mode);
        context.getSource().sendSuccess(() -> Component.translatable("container_loot_tip.command.mode",
                Component.translatable(mode.translationKey())), false);
        return 1;
    }

    private static int setSide(CommandContext<CommandSourceStack> context) {
        PanelSide side = PanelSide.parse(StringArgumentType.getString(context, "value"));

        if (side == null) {
            return invalid(context);
        }

        ClientSettings.get().setPanelSide(side);
        context.getSource().sendSuccess(() -> Component.translatable("container_loot_tip.command.side",
                Component.translatable(side.translationKey())), false);
        return 1;
    }

    private static int setDetail(CommandContext<CommandSourceStack> context) {
        DetailLevel level = DetailLevel.parse(StringArgumentType.getString(context, "value"));

        if (level == null) {
            return invalid(context);
        }

        ClientSettings.get().setDetailLevel(level);
        context.getSource().sendSuccess(() -> Component.translatable("container_loot_tip.command.detail",
                Component.translatable(level.translationKey())), false);
        return 1;
    }

    private static int setCollapsed(CommandContext<CommandSourceStack> context) {
        String value = StringArgumentType.getString(context, "value");
        boolean collapsed;

        if ("true".equalsIgnoreCase(value)) {
            collapsed = true;
        } else if ("false".equalsIgnoreCase(value)) {
            collapsed = false;
        } else {
            return invalid(context);
        }

        ClientSettings.get().setCollapsed(collapsed);
        context.getSource().sendSuccess(() -> Component.translatable("container_loot_tip.command.collapsed",
                Component.translatable(collapsed ? "container_loot_tip.value.yes" : "container_loot_tip.value.no")), false);
        return 1;
    }

    private static int invalid(CommandContext<CommandSourceStack> context) {
        context.getSource().sendFailure(Component.translatable("container_loot_tip.command.invalid"));
        return 0;
    }
}
