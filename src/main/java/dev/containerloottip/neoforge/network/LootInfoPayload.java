package dev.containerloottip.neoforge.network;

import dev.containerloottip.ContainerLootTip;
import dev.containerloottip.client.ClientLootInfoCache;
import dev.containerloottip.info.ContainerLootInfo;
import dev.containerloottip.info.LootInfoCodec;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.neoforge.network.handling.IPayloadContext;

/** Payload carrying one container's loot metadata from the server to the client. */
public record LootInfoPayload(ContainerLootInfo info) implements CustomPacketPayload {
    public static final Type<LootInfoPayload> TYPE =
            new Type<>(ResourceLocation.fromNamespaceAndPath(ContainerLootTip.MOD_ID, "loot_info"));
    public static final StreamCodec<RegistryFriendlyByteBuf, LootInfoPayload> STREAM_CODEC = StreamCodec.of(
            (buffer, payload) -> LootInfoCodec.write(buffer, payload.info),
            buffer -> new LootInfoPayload(LootInfoCodec.read(buffer))
    );

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }

    public static void handle(LootInfoPayload payload, IPayloadContext context) {
        context.enqueueWork(() -> ClientLootInfoCache.put(payload.info));
    }
}
