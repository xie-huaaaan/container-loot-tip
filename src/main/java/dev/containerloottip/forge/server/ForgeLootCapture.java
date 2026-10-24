package dev.containerloottip.forge.server;

import dev.containerloottip.ContainerLootTip;
import dev.containerloottip.forge.compat.BlockKinds;
import dev.containerloottip.forge.compat.LootrCompat;
import dev.containerloottip.info.CapturedLootInfo;
import dev.containerloottip.info.ContainerKind;
import dev.containerloottip.info.ContainerLootInfo;
import dev.containerloottip.info.LootSource;
import dev.containerloottip.platform.Platform;
import dev.containerloottip.platform.PlatformHolder;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.vehicle.AbstractMinecartContainer;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.level.block.ChestBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.RandomizableContainerBlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.ChestType;
import net.minecraftforge.event.entity.player.PlayerContainerEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * Captures the loot table before a container menu is created, then sends it once the menu id is known.
 *
 * <p>Vanilla containers clear their table in {@code createMenu}; the interaction event is the last point
 * where the table is still available without touching {@code ServerPlayer#openMenu}. The pending entry
 * is deliberately short-lived so a right-click that never opens a menu cannot be attributed to a later one.</p>
 */
@Mod.EventBusSubscriber(modid = ContainerLootTip.MOD_ID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public final class ForgeLootCapture {
    private static final long MAX_PENDING_AGE = 10L;
    private static final Map<UUID, Pending> PENDING = new HashMap<>();

    private ForgeLootCapture() {
    }

    @SubscribeEvent
    public static void onRightClickBlock(PlayerInteractEvent.RightClickBlock event) {
        if (!(event.getEntity() instanceof ServerPlayer player) || event.getLevel().isClientSide || player.isSpectator()) {
            return;
        }

        CapturedLootInfo info = captureBlock((ServerLevel) event.getLevel(), event.getPos());

        if (info != null) {
            PENDING.put(player.getUUID(), new Pending(info, player.serverLevel().getGameTime()));
        }
    }

    @SubscribeEvent
    public static void onEntityInteract(PlayerInteractEvent.EntityInteract event) {
        if (!(event.getEntity() instanceof ServerPlayer player) || event.getLevel().isClientSide || player.isSpectator()) {
            return;
        }

        CapturedLootInfo info = captureEntity(event.getTarget());

        if (info != null) {
            PENDING.put(player.getUUID(), new Pending(info, player.serverLevel().getGameTime()));
        }
    }

    @SubscribeEvent
    public static void onContainerOpen(PlayerContainerEvent.Open event) {
        if (!(event.getEntity() instanceof ServerPlayer player)) {
            return;
        }

        Pending pending = PENDING.remove(player.getUUID());

        if (pending == null) {
            return;
        }

        long age = player.serverLevel().getGameTime() - pending.gameTime;

        if (age < 0 || age > MAX_PENDING_AGE) {
            return;
        }

        AbstractContainerMenu menu = event.getContainer();

        if (menu != player.containerMenu) {
            return;
        }

        int menuSize = Math.max(0, menu.slots.size() - player.getInventory().getContainerSize());
        ContainerLootInfo info = pending.info.toInfo(menu.containerId, menuSize);
        Platform platform = PlatformHolder.get();

        if (platform.isLootInfoChannelPresent(player)) {
            platform.sendLootInfo(player, info);
        }
    }

    @SubscribeEvent
    public static void onLogout(PlayerEvent.PlayerLoggedOutEvent event) {
        PENDING.remove(event.getEntity().getUUID());
    }

    private static CapturedLootInfo captureBlock(ServerLevel level, BlockPos pos) {
        BlockEntity blockEntity = level.getBlockEntity(pos);

        if (PlatformHolder.get().isModLoaded("lootr") && LootrCompat.isLootrBlockEntity(blockEntity)) {
            return LootrCompat.captureBlock(blockEntity);
        }

        if (blockEntity instanceof RandomizableContainerBlockEntity container) {
            ResourceLocation table = container.lootTable;

            if (table == null) {
                RandomizableContainerBlockEntity connected = connectedChest(level, pos);

                if (connected != null) {
                    container = connected;
                    table = connected.lootTable;
                }
            }

            if (table == null) {
                return null;
            }

            return new CapturedLootInfo(
                    table,
                    LootSource.VANILLA,
                    BlockKinds.kind(blockEntity),
                    container.lootTableSeed,
                    false,
                    ContainerLootInfo.UNKNOWN_INT,
                    ContainerLootInfo.UNKNOWN_INT,
                    ContainerLootInfo.UNKNOWN_INT,
                    container.getContainerSize()
            );
        }

        return null;
    }

    private static RandomizableContainerBlockEntity connectedChest(ServerLevel level, BlockPos pos) {
        BlockState state = level.getBlockState(pos);

        if (!(state.getBlock() instanceof ChestBlock) || state.getValue(ChestBlock.TYPE) == ChestType.SINGLE) {
            return null;
        }

        BlockEntity neighbor = level.getBlockEntity(pos.relative(ChestBlock.getConnectedDirection(state)));

        if (neighbor instanceof RandomizableContainerBlockEntity container && container.lootTable != null) {
            return container;
        }

        return null;
    }

    private static CapturedLootInfo captureEntity(Entity target) {
        if (!(target instanceof AbstractMinecartContainer cart)) {
            return null;
        }

        if (PlatformHolder.get().isModLoaded("lootr") && LootrCompat.isLootrEntity(target)) {
            return LootrCompat.captureEntity(target);
        }

        ResourceLocation table = cart.getLootTable();

        if (table == null) {
            return null;
        }

        return new CapturedLootInfo(
                table,
                LootSource.VANILLA,
                ContainerKind.MINECART,
                cart.getLootTableSeed(),
                false,
                ContainerLootInfo.UNKNOWN_INT,
                ContainerLootInfo.UNKNOWN_INT,
                ContainerLootInfo.UNKNOWN_INT,
                cart.getContainerSize()
        );
    }

    private record Pending(CapturedLootInfo info, long gameTime) {
    }
}
