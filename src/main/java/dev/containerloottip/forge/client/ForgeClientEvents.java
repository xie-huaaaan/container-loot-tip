package dev.containerloottip.forge.client;

import dev.containerloottip.ContainerLootTip;
import dev.containerloottip.client.ClientLootInfoCache;
import dev.containerloottip.client.LootInfoPanel;
import dev.containerloottip.info.ContainerLootInfo;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.ClientPlayerNetworkEvent;
import net.minecraftforge.client.event.ContainerScreenEvent;
import net.minecraftforge.client.event.ScreenEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

/** Client hooks that draw and route clicks to the shared panel. */
@Mod.EventBusSubscriber(modid = ContainerLootTip.MOD_ID, value = Dist.CLIENT, bus = Mod.EventBusSubscriber.Bus.FORGE)
public final class ForgeClientEvents {
    private ForgeClientEvents() {
    }

    @SubscribeEvent
    public static void onRender(ContainerScreenEvent.Render.Foreground event) {
        AbstractContainerScreen<?> screen = event.getContainerScreen();
        ContainerLootInfo info = ClientLootInfoCache.get(screen.getMenu().containerId);

        if (info != null) {
            LootInfoPanel.render(event.getGuiGraphics(), screen, event.getMouseX(), event.getMouseY(), info);
        }
    }

    @SubscribeEvent
    public static void onMousePressed(ScreenEvent.MouseButtonPressed.Pre event) {
        if (!(event.getScreen() instanceof AbstractContainerScreen<?> screen)) {
            return;
        }

        ContainerLootInfo info = ClientLootInfoCache.get(screen.getMenu().containerId);

        if (info != null && LootInfoPanel.mouseClicked(event.getMouseX(), event.getMouseY(), event.getButton(), screen, info)) {
            event.setCanceled(true);
        }
    }

    @SubscribeEvent
    public static void onLogout(ClientPlayerNetworkEvent.LoggingOut event) {
        ClientLootInfoCache.clear();
    }
}
