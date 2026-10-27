package dev.containerloottip.config;

/** Side of the container GUI where the sidebar is placed. */
public enum PanelSide {
    LEFT("container_loot_tip.side.left"),
    RIGHT("container_loot_tip.side.right");

    private final String translationKey;

    PanelSide(String translationKey) {
        this.translationKey = translationKey;
    }

    public String translationKey() {
        return translationKey;
    }

    public static PanelSide byName(String name) {
        PanelSide side = parse(name);
        return side == null ? LEFT : side;
    }

    public static PanelSide parse(String name) {
        if (name != null) {
            String normalized = name.trim().replace('-', '_');

            for (PanelSide side : values()) {
                if (side.name().equalsIgnoreCase(normalized)) {
                    return side;
                }
            }
        }

        return null;
    }
}
