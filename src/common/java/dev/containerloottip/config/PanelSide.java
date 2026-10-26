package dev.containerloottip.config;

/** Side of the container GUI where the sidebar is placed. */
public enum PanelSide {
    LEFT,
    RIGHT;

    public static PanelSide byName(String name) {
        if (name != null) {
            String normalized = name.trim().replace('-', '_');

            for (PanelSide side : values()) {
                if (side.name().equalsIgnoreCase(normalized)) {
                    return side;
                }
            }
        }

        return LEFT;
    }
}
