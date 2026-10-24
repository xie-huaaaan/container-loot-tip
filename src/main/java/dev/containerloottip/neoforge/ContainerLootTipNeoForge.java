package dev.containerloottip.neoforge;

import dev.containerloottip.ContainerLootTip;
import dev.containerloottip.platform.PlatformHolder;
import net.neoforged.fml.common.Mod;

@Mod(ContainerLootTip.MOD_ID)
public final class ContainerLootTipNeoForge {
    public ContainerLootTipNeoForge() {
        PlatformHolder.set(new NeoForgePlatform());
        ContainerLootTip.LOGGER.info("{} initialized", ContainerLootTip.MOD_NAME);
    }
}
