package dev.containerloottip.forge.client.link;

import dev.containerloottip.ContainerLootTip;
import dev.containerloottip.client.link.TableOpeners;
import dev.containerloottip.platform.Platform;
import dev.containerloottip.platform.PlatformHolder;
import mezz.jei.api.IModPlugin;
import mezz.jei.api.JeiPlugin;
import mezz.jei.api.runtime.IJeiRuntime;
import net.minecraft.resources.ResourceLocation;

/**
 * JEI entry point. This class intentionally has no ALI imports: JEI loads it whenever JEI is present,
 * and the ALI-specific lookup class must only be loaded after ALI was confirmed present.
 */
@JeiPlugin
public final class JeiPluginEntry implements IModPlugin {
    private static final ResourceLocation UID = new ResourceLocation(ContainerLootTip.MOD_ID, "jei");
    private static volatile IJeiRuntime runtime;
    private static volatile boolean registered;

    @Override
    public ResourceLocation getPluginUid() {
        return UID;
    }

    @Override
    public void onRuntimeAvailable(IJeiRuntime jeiRuntime) {
        runtime = jeiRuntime;
        Platform platform = PlatformHolder.get();

        if (!registered && platform.isModLoaded("ali") && platform.isModLoaded("aci")) {
            TableOpeners.register(JeiPluginEntry::open);
            registered = true;
        }
    }

    @Override
    public void onRuntimeUnavailable() {
        runtime = null;
    }

    private static boolean open(ResourceLocation tableId) {
        IJeiRuntime jeiRuntime = runtime;
        return jeiRuntime != null && AliJeiLookup.open(jeiRuntime, tableId);
    }
}
