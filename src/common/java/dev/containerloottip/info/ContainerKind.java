package dev.containerloottip.info;

/**
 * Coarse container classification shown in the panel. It is intentionally loader- and mod-agnostic:
 * the server derives it from the menu provider so the client never has to inspect block entities.
 */
public enum ContainerKind {
    CHEST("container_loot_tip.kind.chest"),
    BARREL("container_loot_tip.kind.barrel"),
    SHULKER("container_loot_tip.kind.shulker"),
    MINECART("container_loot_tip.kind.minecart"),
    OTHER("container_loot_tip.kind.other");

    private final String translationKey;

    ContainerKind(String translationKey) {
        this.translationKey = translationKey;
    }

    public String translationKey() {
        return translationKey;
    }
}
