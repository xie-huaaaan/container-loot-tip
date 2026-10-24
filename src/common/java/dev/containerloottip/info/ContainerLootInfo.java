package dev.containerloottip.info;

import net.minecraft.resources.ResourceLocation;

/**
 * Server-authoritative snapshot of a container's loot table, sent to the client when a menu is opened.
 *
 * @param menuId       container menu id the data belongs to
 * @param tableId      loot table opened by the container
 * @param source       vanilla container or Lootr
 * @param kind         coarse container classification
 * @param size         number of loot slots
 * @param seed         loot seed, or {@link #UNKNOWN_LONG} when the source does not expose one
 * @param openedBefore whether the container had already been opened before this interaction
 * @param openers      number of players that opened the container, or -1 when unknown
 * @param refreshTicks ticks until Lootr refreshes the loot, or -1 when not scheduled
 * @param decayTicks   ticks until Lootr removes the container contents, or -1 when not scheduled
 */
public record ContainerLootInfo(
        int menuId,
        ResourceLocation tableId,
        LootSource source,
        ContainerKind kind,
        int size,
        long seed,
        boolean openedBefore,
        int openers,
        int refreshTicks,
        int decayTicks
) {
    public static final int UNKNOWN_INT = -1;
    public static final long UNKNOWN_LONG = Long.MIN_VALUE;
}
