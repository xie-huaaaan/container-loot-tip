package dev.containerloottip.neoforge;

import dev.containerloottip.info.ContainerLootInfo;
import dev.containerloottip.neoforge.network.NeoForgeNetworkSender;
import dev.containerloottip.platform.Platform;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.neoforged.fml.ModList;
import net.neoforged.fml.loading.FMLPaths;

import java.nio.file.Path;

final class NeoForgePlatform implements Platform {
    @Override
    public boolean isModLoaded(String modId) {
        return ModList.get().isLoaded(modId);
    }

    @Override
    public Path configDir() {
        return FMLPaths.CONFIGDIR.get();
    }

    @Override
    public ResourceLocation id(String namespace, String path) {
        return ResourceLocation.fromNamespaceAndPath(namespace, path);
    }

    @Override
    public boolean isLootInfoChannelPresent(ServerPlayer player) {
        return NeoForgeNetworkSender.isPresent(player);
    }

    @Override
    public void sendLootInfo(ServerPlayer player, ContainerLootInfo info) {
        NeoForgeNetworkSender.send(player, info);
    }
}
