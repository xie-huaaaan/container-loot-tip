package dev.containerloottip.client;

import dev.containerloottip.info.ContainerLootInfo;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Client-side store for loot info received from the server, keyed by container menu id.
 *
 * <p>The server sends the payload immediately after opening the menu, which may happen after the
 * client screen was initialised. Rendering therefore looks the data up every frame instead of
 * copying it into the screen once.</p>
 */
public final class ClientLootInfoCache {
    private static final int MAX_ENTRIES = 16;
    private static final Map<Integer, ContainerLootInfo> INFO = new LinkedHashMap<>(16, 0.75F, true) {
        @Override
        protected boolean removeEldestEntry(Map.Entry<Integer, ContainerLootInfo> eldest) {
            return size() > MAX_ENTRIES;
        }
    };

    private ClientLootInfoCache() {
    }

    public static synchronized void put(ContainerLootInfo info) {
        INFO.put(info.menuId(), info);
    }

    public static synchronized ContainerLootInfo get(int menuId) {
        return INFO.get(menuId);
    }

    public static synchronized void remove(int menuId) {
        INFO.remove(menuId);
    }

    public static synchronized void clear() {
        INFO.clear();
    }
}
