package dev.containerloottip.info;

/**
 * Coarse container classification shown in the panel. It is intentionally loader- and mod-agnostic:
 * the server derives it from the menu provider so the client never has to inspect block entities.
 */
public enum ContainerKind {
    CHEST("containerloottip.kind.chest"),
    BARREL("containerloottip.kind.barrel"),
    SHULKER("containerloottip.kind.shulker"),
    MINECART("containerloottip.kind.minecart"),
    OTHER("containerloottip.kind.other");

    private final String translationKey;

    ContainerKind(String translationKey) {
        this.translationKey = translationKey;
    }

    public String translationKey() {
        return translationKey;
    }
}
