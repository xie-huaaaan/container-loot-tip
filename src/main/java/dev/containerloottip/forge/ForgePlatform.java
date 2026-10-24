package dev.containerloottip.forge;

import dev.containerloottip.forge.network.ForgeNetwork;
import dev.containerloottip.info.ContainerLootInfo;
import dev.containerloottip.platform.Platform;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.fml.ModList;
import net.minecraftforge.fml.loading.FMLPaths;

import java.nio.file.Path;

final class ForgePlatform implements Platform {
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
        return new ResourceLocation(namespace, path);
    }

    @Override
    public boolean isLootInfoChannelPresent(ServerPlayer player) {
        return ForgeNetwork.isPresent(player);
    }

    @Override
    public void sendLootInfo(ServerPlayer player, ContainerLootInfo info) {
        ForgeNetwork.send(player, info);
    }
}
