package dev.container_loot_tip.config;

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
        for (DetailLevel level : values()) {
            if (level.name().equals(name)) {
                return level;
            }
        }

        return COMPACT;
    }
}
