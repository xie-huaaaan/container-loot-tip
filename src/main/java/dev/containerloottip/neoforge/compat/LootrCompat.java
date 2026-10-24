package dev.containerloottip.neoforge.compat;

import dev.containerloottip.info.CapturedLootInfo;
import dev.containerloottip.info.ContainerKind;
import dev.containerloottip.info.LootSource;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.storage.loot.LootTable;
import noobanidus.mods.lootr.common.api.LootrAPI;
import noobanidus.mods.lootr.common.api.data.ILootrInfoProvider;
import noobanidus.mods.lootr.common.api.data.blockentity.ILootrBlockEntity;
import noobanidus.mods.lootr.common.api.data.entity.ILootrEntity;

import java.util.Set;
import java.util.UUID;

/**
 * Lootr 1.11+ integration. Loaded only after {@code lootr} was confirmed present so its classes
 * never reach the classpath in a vanilla-only or Lootr-less installation.
 */
public final class LootrCompat {
    private LootrCompat() {
    }

    public static boolean isLootrBlockEntity(BlockEntity blockEntity) {
        return LootrAPI.resolveBlockEntity(blockEntity) instanceof ILootrBlockEntity;
    }

    public static boolean isLootrEntity(Entity entity) {
        return LootrAPI.resolveEntity(entity) instanceof ILootrEntity;
    }

    public static CapturedLootInfo captureBlock(BlockEntity blockEntity) {
        ILootrBlockEntity lootr = LootrAPI.resolveBlockEntity(blockEntity);

        if (lootr == null) {
            return null;
        }

        return capture(lootr, BlockKinds.kind(blockEntity));
    }

    public static CapturedLootInfo captureEntity(Entity entity) {
        ILootrEntity lootr = LootrAPI.resolveEntity(entity);

        if (lootr == null) {
            return null;
        }

        return capture(lootr, ContainerKind.MINECART);
    }

    private static CapturedLootInfo capture(ILootrInfoProvider info, ContainerKind kind) {
        ResourceKey<LootTable> table = info.getInfoLootTable();

        if (table == null) {
            return null;
        }

        Set<UUID> openers = info.getActualOpeners();
        int openerCount = openers != null ? openers.size() : info.getPhysicalOpenerCount();

        return new CapturedLootInfo(
                table.location(),
                LootSource.LOOTR,
                kind,
                info.getInfoLootSeed(),
                info.hasBeenOpened(),
                openerCount,
                LootrAPI.getRemainingRefreshValue(info),
                LootrAPI.getRemainingDecayValue(info),
                info.getInfoContainerSize()
        );
    }
}
