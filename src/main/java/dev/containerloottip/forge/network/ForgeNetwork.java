package dev.containerloottip.forge.network;

import dev.containerloottip.ContainerLootTip;
import dev.containerloottip.info.ContainerLootInfo;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.network.NetworkRegistry;
import net.minecraftforge.network.PacketDistributor;
import net.minecraftforge.network.simple.SimpleChannel;

/**
 * Forge network channel. The channel is intentionally optional: the handshake accepts vanilla clients,
 * and messages are only sent after checking that the client knows this channel.
 */
public final class ForgeNetwork {
    private static final String PROTOCOL = "1";
    private static SimpleChannel channel;

    private ForgeNetwork() {
    }

    public static void register() {
        channel = NetworkRegistry.newSimpleChannel(
                new ResourceLocation(ContainerLootTip.MOD_ID, "loot_info"),
                () -> PROTOCOL,
                PROTOCOL::equals,
                PROTOCOL::equals
        );
        channel.messageBuilder(LootInfoMessage.class, 0)
                .encoder(LootInfoMessage::encode)
                .decoder(LootInfoMessage::decode)
                .consumerMainThread(LootInfoMessage::handle)
                .add();
    }

    public static boolean isPresent(ServerPlayer player) {
        return channel != null && channel.isRemotePresent(player.connection.connection);
    }

    public static void send(ServerPlayer player, ContainerLootInfo info) {
        if (channel != null) {
            channel.send(PacketDistributor.PLAYER.with(() -> player), new LootInfoMessage(info));
        }
    }
}
