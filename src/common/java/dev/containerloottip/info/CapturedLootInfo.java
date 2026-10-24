package dev.containerloottip.info;

import net.minecraft.resources.ResourceLocation;

/**
 * Loot-table snapshot taken before the menu is created. The menu id and the final slot count are only
 * known after {@code ServerPlayer#openMenu} returns, so they are filled in separately.
 */
public record CapturedLootInfo(
        ResourceLocation tableId,
        LootSource source,
        ContainerKind kind,
        long seed,
        boolean openedBefore,
        int openers,
        int refreshTicks,
        int decayTicks,
        int containerSize
) {
    public ContainerLootInfo toInfo(int menuId, int menuSize) {
        int size = menuSize > 0 ? menuSize : containerSize;
        return new ContainerLootInfo(menuId, tableId, source, kind, Math.max(size, 0), seed, openedBefore, openers, refreshTicks, decayTicks);
    }
}
