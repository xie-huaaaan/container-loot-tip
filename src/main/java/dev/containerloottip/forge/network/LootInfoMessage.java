package dev.containerloottip.forge.network;

import dev.containerloottip.client.ClientLootInfoCache;
import dev.containerloottip.info.ContainerLootInfo;
import dev.containerloottip.info.LootInfoCodec;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

/** Payload carrying one container's loot metadata from the server to the client. */
public record LootInfoMessage(ContainerLootInfo info) {
    public static void encode(LootInfoMessage message, FriendlyByteBuf buffer) {
        LootInfoCodec.write(buffer, message.info);
    }

    public static LootInfoMessage decode(FriendlyByteBuf buffer) {
        return new LootInfoMessage(LootInfoCodec.read(buffer));
    }

    public static void handle(LootInfoMessage message, Supplier<NetworkEvent.Context> contextSupplier) {
        NetworkEvent.Context context = contextSupplier.get();
        context.enqueueWork(() -> ClientLootInfoCache.put(message.info));
        context.setPacketHandled(true);
    }
}
