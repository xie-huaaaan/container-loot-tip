package dev.containerloottip.client.link;

import net.minecraft.resources.ResourceLocation;

/** Opens an ALI loot-table page in a supported recipe viewer. Implementations live in loader modules. */
@FunctionalInterface
public interface TableOpener {
    /** Returns false when ALI does not expose a page for the given table. */
    boolean open(ResourceLocation tableId);
}
