package dev.containerloottip.forge;

import dev.containerloottip.ContainerLootTip;
import dev.containerloottip.forge.network.ForgeNetwork;
import dev.containerloottip.platform.PlatformHolder;
import net.minecraftforge.fml.common.Mod;

@Mod(ContainerLootTip.MOD_ID)
public final class ContainerLootTipForge {
    public ContainerLootTipForge() {
        PlatformHolder.set(new ForgePlatform());
        ForgeNetwork.register();
        ContainerLootTip.LOGGER.info("{} initialized", ContainerLootTip.MOD_NAME);
    }
}
