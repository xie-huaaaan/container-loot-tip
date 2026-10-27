package dev.containerloottip.config;

/** Amount of detail shown by the panel. The button cycles through the values in this order. */
public enum DetailLevel {
    COMPACT("container_loot_tip.detail.compact"),
    DETAILED("container_loot_tip.detail.detailed");

    private final String translationKey;

    DetailLevel(String translationKey) {
        this.translationKey = translationKey;
    }

    public String translationKey() {
        return translationKey;
    }

    public DetailLevel next() {
        DetailLevel[] values = values();
        return values[(ordinal() + 1) % values.length];
    }

    public static DetailLevel byName(String name) {
        DetailLevel level = parse(name);
        return level == null ? COMPACT : level;
    }

    public static DetailLevel parse(String name) {
        if (name != null) {
            String normalized = name.trim();

            for (DetailLevel level : values()) {
                if (level.name().equalsIgnoreCase(normalized)) {
                    return level;
                }
            }
        }

        return null;
    }
}
