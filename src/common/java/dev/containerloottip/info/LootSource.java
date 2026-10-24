package dev.containerloottip.info;

/** Where the container's loot table was captured from. */
public enum LootSource {
    VANILLA("container_loot_tip.source.vanilla"),
    LOOTR("container_loot_tip.source.lootr");

    private final String translationKey;

    LootSource(String translationKey) {
        this.translationKey = translationKey;
    }

    public String translationKey() {
        return translationKey;
    }
}
