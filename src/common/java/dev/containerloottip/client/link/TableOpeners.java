package dev.containerloottip.client.link;

import dev.containerloottip.ContainerLootTip;
import net.minecraft.resources.ResourceLocation;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * Registry of viewer integrations registered by the loader on the client.
 *
 * <p>An empty registry means no JEI/EMI (or no ALI) is available; the panel then renders the table
 * id as plain text instead of a link.</p>
 */
public final class TableOpeners {
    private static final List<TableOpener> OPENERS = new CopyOnWriteArrayList<>();

    private TableOpeners() {
    }

    public static void register(TableOpener opener) {
        OPENERS.add(opener);
    }

    public static boolean canOpen() {
        return !OPENERS.isEmpty();
    }

    public static boolean open(ResourceLocation tableId) {
        for (TableOpener opener : OPENERS) {
            try {
                if (opener.open(tableId)) {
                    return true;
                }
            } catch (Throwable t) {
                ContainerLootTip.LOGGER.warn("Unable to open ALI page for {}", tableId, t);
            }
        }

        return false;
    }
}
