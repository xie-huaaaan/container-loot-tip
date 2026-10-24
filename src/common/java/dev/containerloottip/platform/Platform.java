package dev.containerloottip.platform;

import dev.containerloottip.info.ContainerLootInfo;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;

import java.nio.file.Path;

/** Loader-specific services needed by shared code. */
public interface Platform {
    boolean isModLoaded(String modId);

    Path configDir();

    /** Creates a {@link ResourceLocation} using the version-appropriate factory or constructor. */
    ResourceLocation id(String namespace, String path);

    /** Returns whether the player's connection knows this mod's loot-info channel. */
    boolean isLootInfoChannelPresent(ServerPlayer player);

    /** Sends loot info to a modded client. Must never be called for a client without the channel. */
    void sendLootInfo(ServerPlayer player, ContainerLootInfo info);
}
