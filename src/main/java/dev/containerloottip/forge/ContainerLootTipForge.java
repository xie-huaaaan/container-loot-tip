package dev.containerloottip.forge;

import com.mojang.logging.LogUtils;
import dev.containerloottip.ContainerLootTip;
import dev.containerloottip.platform.PlatformHolder;
import net.minecraftforge.fml.common.Mod;
import org.slf4j.Logger;

@Mod(ContainerLootTip.MOD_ID)
public final class ContainerLootTipForge {
    private static final Logger LOGGER = LogUtils.getLogger();

    public ContainerLootTipForge() {
        PlatformHolder.set(new ForgePlatform());
        LOGGER.info("{} initialized", ContainerLootTip.MOD_NAME);
    }
}
