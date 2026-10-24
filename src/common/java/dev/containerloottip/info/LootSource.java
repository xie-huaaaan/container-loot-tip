package dev.containerloottip.info;

/** Where the container's loot table was captured from. */
public enum LootSource {
    VANILLA("containerloottip.source.vanilla"),
    LOOTR("containerloottip.source.lootr");

    private final String translationKey;

    LootSource(String translationKey) {
        this.translationKey = translationKey;
    }

    public String translationKey() {
        return translationKey;
    }
}
