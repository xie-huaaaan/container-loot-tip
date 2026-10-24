package dev.containerloottip.neoforge;

import com.mojang.logging.LogUtils;
import dev.containerloottip.ContainerLootTip;
import dev.containerloottip.platform.PlatformHolder;
import net.neoforged.fml.common.Mod;
import org.slf4j.Logger;

@Mod(ContainerLootTip.MOD_ID)
public final class ContainerLootTipNeoForge {
    private static final Logger LOGGER = LogUtils.getLogger();

    public ContainerLootTipNeoForge() {
        PlatformHolder.set(new NeoForgePlatform());
        LOGGER.info("{} initialized", ContainerLootTip.MOD_NAME);
    }
}
