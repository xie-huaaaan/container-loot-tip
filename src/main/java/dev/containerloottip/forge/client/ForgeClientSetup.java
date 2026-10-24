package dev.containerloottip.forge.client;

import dev.containerloottip.ContainerLootTip;
import dev.containerloottip.client.link.TableOpeners;
import dev.containerloottip.forge.client.link.EmiTableOpener;
import dev.containerloottip.platform.Platform;
import dev.containerloottip.platform.PlatformHolder;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;

/** Registers viewer integrations that do not expose their own plugin callback. */
@Mod.EventBusSubscriber(modid = ContainerLootTip.MOD_ID, value = Dist.CLIENT, bus = Mod.EventBusSubscriber.Bus.MOD)
public final class ForgeClientSetup {
    private ForgeClientSetup() {
    }

    @SubscribeEvent
    public static void clientSetup(FMLClientSetupEvent event) {
        event.enqueueWork(ForgeClientSetup::registerOpeners);
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
