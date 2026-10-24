package dev.containerloottip.neoforge.client;

import dev.containerloottip.ContainerLootTip;
import dev.containerloottip.client.ClientLootInfoCache;
import dev.containerloottip.client.LootInfoPanel;
import dev.containerloottip.info.ContainerLootInfo;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.ClientPlayerNetworkEvent;
import net.neoforged.neoforge.client.event.ContainerScreenEvent;
import net.neoforged.neoforge.client.event.ScreenEvent;

/** Client hooks that draw and route clicks to the shared panel. */
@EventBusSubscriber(modid = ContainerLootTip.MOD_ID, value = Dist.CLIENT, bus = EventBusSubscriber.Bus.GAME)
public final class NeoForgeClientEvents {
    private NeoForgeClientEvents() {
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
