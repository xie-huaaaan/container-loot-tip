package dev.containerloottip.config;

/** How the client exposes the loot table link. */
public enum PanelMode {
    /** Full sidebar with container metadata. */
    SIDEBAR,
    /** No sidebar; the container title becomes the link. */
    TITLE_LINK;

    public static PanelMode byName(String name) {
        if (name != null) {
            String normalized = name.trim().replace('-', '_');

            for (PanelMode mode : values()) {
                if (mode.name().equalsIgnoreCase(normalized)) {
                    return mode;
                }
            }
        }

        return SIDEBAR;
    }
}
