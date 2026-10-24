package dev.containerloottip.neoforge.network;

import dev.containerloottip.ContainerLootTip;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.network.event.RegisterPayloadHandlersEvent;

/** Registers the optional loot-info payload on both sides. */
@EventBusSubscriber(modid = ContainerLootTip.MOD_ID, bus = EventBusSubscriber.Bus.MOD)
public final class NeoForgeNetwork {
    private NeoForgeNetwork() {
    }

    @SubscribeEvent
    public static void register(RegisterPayloadHandlersEvent event) {
        event.registrar("1")
                .optional()
                .playToClient(LootInfoPayload.TYPE, LootInfoPayload.STREAM_CODEC, LootInfoPayload::handle);
    }
}
