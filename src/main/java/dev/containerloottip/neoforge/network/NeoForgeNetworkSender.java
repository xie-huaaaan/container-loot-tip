package dev.containerloottip.neoforge.network;

import dev.containerloottip.info.ContainerLootInfo;
import net.minecraft.server.level.ServerPlayer;
import net.neoforged.neoforge.network.PacketDistributor;
import net.neoforged.neoforge.network.registration.NetworkRegistry;

/** Send-side helpers for the optional payload. */
public final class NeoForgeNetworkSender {
    private NeoForgeNetworkSender() {
    }

    public static boolean isPresent(ServerPlayer player) {
        return NetworkRegistry.hasChannel(player.connection, LootInfoPayload.TYPE.id());
    }

    public static void send(ServerPlayer player, ContainerLootInfo info) {
        PacketDistributor.sendToPlayer(player, new LootInfoPayload(info));
    }
}
