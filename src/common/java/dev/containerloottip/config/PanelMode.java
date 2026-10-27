package dev.containerloottip.config;

/** How the client exposes the loot table link. */
public enum PanelMode {
    /** Full sidebar with container metadata. */
    SIDEBAR("container_loot_tip.mode.sidebar"),
    /** No sidebar; the container title becomes the link. */
    TITLE_LINK("container_loot_tip.mode.title_link");

    private final String translationKey;

    PanelMode(String translationKey) {
        this.translationKey = translationKey;
    }

    public String translationKey() {
        return translationKey;
    }

    public static PanelMode byName(String name) {
        PanelMode mode = parse(name);
        return mode == null ? SIDEBAR : mode;
    }

    public static PanelMode parse(String name) {
        if (name != null) {
            String normalized = name.trim().replace('-', '_');

            for (PanelMode mode : values()) {
                if (mode.name().equalsIgnoreCase(normalized)) {
                    return mode;
                }
            }
        }

        return null;
    }
}
