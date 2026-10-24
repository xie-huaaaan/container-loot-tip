package dev.containerloottip.forge.compat;

import dev.containerloottip.info.CapturedLootInfo;
import dev.containerloottip.info.ContainerKind;
import dev.containerloottip.info.ContainerLootInfo;
import dev.containerloottip.info.LootSource;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.Container;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.vehicle.AbstractMinecartContainer;
import net.minecraft.world.level.block.entity.BlockEntity;
import noobanidus.mods.lootr.api.IHasOpeners;
import noobanidus.mods.lootr.api.blockentity.ILootBlockEntity;
import noobanidus.mods.lootr.api.entity.ILootCart;
import noobanidus.mods.lootr.data.DataStorage;

import java.util.Set;
import java.util.UUID;

/**
 * Lootr 1.20.1 integration. This class is only loaded after {@code lootr} was confirmed present,
 * keeping Lootr classes off the classpath when the mod is installed without it.
 */
public final class LootrCompat {
    private LootrCompat() {
    }

    public static boolean isLootrBlockEntity(BlockEntity blockEntity) {
        return blockEntity instanceof ILootBlockEntity;
    }

    public static boolean isLootrEntity(Entity entity) {
        return entity instanceof ILootCart;
    }

    public static CapturedLootInfo captureBlock(BlockEntity blockEntity) {
        ILootBlockEntity lootr = (ILootBlockEntity) blockEntity;
        ResourceLocation table = lootr.getTable();

        if (table == null) {
            return null;
        }

        Set<UUID> openers = lootr.getOpeners();
        int openerCount = openers == null ? ContainerLootInfo.UNKNOWN_INT : openers.size();
        UUID tileId = lootr.getTileId();
        int refresh = tileId == null ? ContainerLootInfo.UNKNOWN_INT : DataStorage.getRefreshValue(tileId);
        int decay = tileId == null ? ContainerLootInfo.UNKNOWN_INT : DataStorage.getDecayValue(tileId);
        int size = blockEntity instanceof Container container ? container.getContainerSize() : 0;

        return new CapturedLootInfo(
                table,
                LootSource.LOOTR,
                BlockKinds.kind(blockEntity),
                lootr.getSeed(),
                openers != null && !openers.isEmpty(),
                openerCount,
                refresh,
                decay,
                size
        );
    }

    public static CapturedLootInfo captureEntity(Entity entity) {
        AbstractMinecartContainer cart = (AbstractMinecartContainer) entity;
        ResourceLocation table = cart.getLootTable();

        if (table == null) {
            return null;
        }

        int openerCount = ContainerLootInfo.UNKNOWN_INT;
        boolean opened = false;
        int refresh = ContainerLootInfo.UNKNOWN_INT;
        int decay = ContainerLootInfo.UNKNOWN_INT;

        if (entity instanceof IHasOpeners openable) {
            Set<UUID> openers = openable.getOpeners();
            openerCount = openers == null ? ContainerLootInfo.UNKNOWN_INT : openers.size();
            opened = openers != null && !openers.isEmpty();
            refresh = DataStorage.getRefreshValue(entity.getUUID());
            decay = DataStorage.getDecayValue(entity.getUUID());
        }

        return new CapturedLootInfo(
                table,
                LootSource.LOOTR,
                ContainerKind.MINECART,
                cart.getLootTableSeed(),
                opened,
                openerCount,
                refresh,
                decay,
                cart.getContainerSize()
        );
    }
}
