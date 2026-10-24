package dev.containerloottip.neoforge.client;

import dev.containerloottip.ContainerLootTip;
import dev.containerloottip.client.link.TableOpeners;
import dev.containerloottip.neoforge.client.link.EmiTableOpener;
import dev.containerloottip.platform.Platform;
import dev.containerloottip.platform.PlatformHolder;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent;

/** Registers viewer integrations that do not expose their own plugin callback. */
@EventBusSubscriber(modid = ContainerLootTip.MOD_ID, value = Dist.CLIENT, bus = EventBusSubscriber.Bus.MOD)
public final class NeoForgeClientSetup {
    private NeoForgeClientSetup() {
    }

    @SubscribeEvent
    public static void clientSetup(FMLClientSetupEvent event) {
        event.enqueueWork(NeoForgeClientSetup::registerOpeners);
    }

    private static void registerOpeners() {
        Platform platform = PlatformHolder.get();

        if (!platform.isModLoaded("ali") || !platform.isModLoaded("aci")) {
            return;
        }

        if (platform.isModLoaded("emi")) {
            TableOpeners.register(new EmiTableOpener());
        }
    }
}
