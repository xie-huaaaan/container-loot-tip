package dev.containerloottip.neoforge.compat;

import dev.containerloottip.info.ContainerKind;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.ShulkerBoxBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;

/** 1.21.1 container classification used by the vanilla and Lootr capture paths. */
public final class BlockKinds {
    private BlockKinds() {
    }

    public static ContainerKind kind(BlockEntity blockEntity) {
        return kind(blockEntity.getBlockState());
    }

    public static ContainerKind kind(BlockState state) {
        if (state.getBlock() instanceof ShulkerBoxBlock) {
            return ContainerKind.SHULKER;
        }

        ResourceLocation id = BuiltInRegistries.BLOCK.getKey(state.getBlock());
        String path = id.getPath();

        if (path.contains("barrel")) {
            return ContainerKind.BARREL;
        }

        if (path.contains("chest")) {
            return ContainerKind.CHEST;
        }

        return ContainerKind.OTHER;
    }
}
