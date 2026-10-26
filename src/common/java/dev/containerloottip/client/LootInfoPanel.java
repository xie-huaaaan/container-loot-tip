package dev.containerloottip.client;

import dev.containerloottip.client.link.TableOpeners;
import dev.containerloottip.config.ClientSettings;
import dev.containerloottip.config.DetailLevel;
import dev.containerloottip.config.PanelMode;
import dev.containerloottip.config.PanelSide;
import dev.containerloottip.info.ContainerKind;
import dev.containerloottip.info.ContainerLootInfo;
import dev.containerloottip.info.LootSource;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.util.FormattedCharSequence;

import java.util.ArrayList;
import java.util.List;

/**
 * Loot information overlay drawn on top of container screens.
 *
 * <p>The sidebar can be placed on either side, or replaced by a link on the container title.
 * Coordinates are relative to the container GUI origin because both loaders fire the foreground
 * screen event inside a pose already translated by the GUI position. Mouse events arrive in absolute
 * screen coordinates, so they are converted back before hit-testing.</p>
 */
public final class LootInfoPanel {
    private static final int WIDTH = 156;
    private static final int TAB_WIDTH = 14;
    private static final int TAB_HEIGHT = 30;
    private static final int PAD = 6;
    private static final int LINE = 10;
    private static final int HEADER = 12;
    private static final int FOOTER = 12;

    private static final int COLOR_BACKGROUND = 0xE8101014;
    private static final int COLOR_BORDER = 0xFF3C3C48;
    private static final int COLOR_SEPARATOR = 0xFF2A2A34;
    private static final int COLOR_TITLE = 0xFFFFFFFF;
    private static final int COLOR_TEXT = 0xFFB8B8C8;
    private static final int COLOR_LINK = 0xFF66FFFF;
    private static final int COLOR_LINK_HOVER = 0xFFFFFFFF;
    private static final int COLOR_PLAIN = 0xFFE0E0E0;
    private static final int COLOR_HOVER = 0x30FFFFFF;

    private static Box panelBox;
    private static Box collapseBox;
    private static Box detailBox;
    private static Box linkBox;
    private static Box tabBox;
    private static Box titleLinkBox;

    private LootInfoPanel() {
    }

    public static void render(GuiGraphics graphics, AbstractContainerScreen<?> screen, int mouseX, int mouseY, ContainerLootInfo info) {
        panelBox = null;
        collapseBox = null;
        detailBox = null;
        linkBox = null;
        tabBox = null;
        titleLinkBox = null;

        int relativeMouseX = mouseX - screen.getGuiLeft();
        int relativeMouseY = mouseY - screen.getGuiTop();
        ClientSettings settings = ClientSettings.get();

        if (settings.panelMode() == PanelMode.TITLE_LINK) {
            renderTitleLink(graphics, screen, relativeMouseX, relativeMouseY);
        } else if (settings.isCollapsed()) {
            renderTab(graphics, screen, relativeMouseX, relativeMouseY, settings.panelSide());
        } else {
            renderExpanded(graphics, screen, relativeMouseX, relativeMouseY, info, settings);
        }
    }

    public static boolean mouseClicked(double mouseX, double mouseY, int button, AbstractContainerScreen<?> screen, ContainerLootInfo info) {
        if (button != 0) {
            return false;
        }

        double relativeX = mouseX - screen.getGuiLeft();
        double relativeY = mouseY - screen.getGuiTop();
        ClientSettings settings = ClientSettings.get();

        if (settings.panelMode() == PanelMode.TITLE_LINK) {
            return titleLinkClicked(relativeX, relativeY, info);
        }

        if (settings.isCollapsed()) {
            if (tabBox != null && tabBox.contains(relativeX, relativeY)) {
                settings.setCollapsed(false);
                return true;
            }

            return false;
        }

        if (collapseBox != null && collapseBox.contains(relativeX, relativeY)) {
            settings.setCollapsed(true);
            return true;
        }

        if (detailBox != null && detailBox.contains(relativeX, relativeY)) {
            settings.cycleDetailLevel();
            message("container_loot_tip.detail.changed", Component.translatable(settings.detailLevel().translationKey()));
            return true;
        }

        if (linkBox != null && linkBox.contains(relativeX, relativeY)) {
            if (Screen.hasShiftDown()) {
                copyTableId(info);
            } else if (TableOpeners.canOpen()) {
                openTable(info);
            }

            return true;
        }

        // Consume clicks on the panel surface so a panel overlapping the GUI cannot click through to slots.
        return panelBox != null && panelBox.contains(relativeX, relativeY);
    }

    private static void renderTab(GuiGraphics graphics, AbstractContainerScreen<?> screen, int mouseX, int mouseY,
                                  PanelSide side) {
        int x = panelX(screen, TAB_WIDTH, side);
        int y = 4;
        boolean hovered = mouseX >= x && mouseX < x + TAB_WIDTH && mouseY >= y && mouseY < y + TAB_HEIGHT;

        drawBackground(graphics, x, y, TAB_WIDTH, TAB_HEIGHT);

        if (hovered) {
            graphics.fill(x + 1, y + 1, x + TAB_WIDTH - 1, y + TAB_HEIGHT - 1, COLOR_HOVER);
        }

        String arrow = side == PanelSide.RIGHT ? "»" : "«";
        graphics.drawCenteredString(Minecraft.getInstance().font, arrow, x + TAB_WIDTH / 2, y + TAB_HEIGHT / 2 - 4, COLOR_LINK);
        tabBox = new Box(x, y, TAB_WIDTH, TAB_HEIGHT);
        panelBox = tabBox;
    }

    private static void renderExpanded(GuiGraphics graphics, AbstractContainerScreen<?> screen, int mouseX, int mouseY,
                                       ContainerLootInfo info, ClientSettings settings) {
        Font font = Minecraft.getInstance().font;
        boolean canLink = TableOpeners.canOpen();
        List<FormattedCharSequence> tableLines = font.split(Component.literal(info.tableId().toString()), WIDTH - PAD * 2 - 2);
        List<Line> lines = buildLines(info, settings.detailLevel());

        int tableHeight = tableLines.size() * LINE;
        int contentHeight = PAD + HEADER + 2 + tableHeight + 6 + lines.size() * LINE + 6 + FOOTER + PAD;
        int x = panelX(screen, WIDTH, settings.panelSide());
        int y = 4;
        int availableHeight = screen.height - screen.getGuiTop();

        if (y + contentHeight > availableHeight - 2) {
            y = Math.max(2, availableHeight - contentHeight - 2);
        }

        drawBackground(graphics, x, y, WIDTH, contentHeight);
        graphics.fill(x + PAD, y + PAD + HEADER + 1, x + WIDTH - PAD, y + PAD + HEADER + 2, COLOR_SEPARATOR);

        graphics.drawString(font, Component.translatable("container_loot_tip.panel.title"), x + PAD, y + PAD, COLOR_TITLE, true);

        int collapseX = x + WIDTH - PAD - 9;
        int collapseY = y + PAD - 1;
        boolean collapseHovered = mouseX >= collapseX && mouseX < collapseX + 10 && mouseY >= collapseY && mouseY < collapseY + 10;

        if (collapseHovered) {
            graphics.fill(collapseX - 1, collapseY - 1, collapseX + 10, collapseY + 10, COLOR_HOVER);
        }

        String collapseArrow = settings.panelSide() == PanelSide.RIGHT ? "«" : "»";
        graphics.drawString(font, collapseArrow, collapseX + 1, collapseY + 1, collapseHovered ? COLOR_TITLE : COLOR_TEXT, false);
        collapseBox = new Box(collapseX - 1, collapseY - 1, 12, 12);

        int linkY = y + PAD + HEADER + 4;
        boolean linkHovered = mouseX >= x + 2 && mouseX < x + WIDTH - 2 && mouseY >= linkY - 1 && mouseY < linkY + tableHeight + 1;
        int linkColor = !canLink ? COLOR_PLAIN : (linkHovered ? COLOR_LINK_HOVER : COLOR_LINK);

        for (int i = 0; i < tableLines.size(); i++) {
            FormattedCharSequence line = tableLines.get(i);
            int lineY = linkY + i * LINE;
            graphics.drawString(font, line, x + PAD, lineY, linkColor, false);

            if (canLink) {
                graphics.fill(x + PAD, lineY + LINE - 1, x + PAD + font.width(line), lineY + LINE, linkColor);
            }
        }

        linkBox = new Box(x + 2, linkY - 1, WIDTH - 4, tableHeight + 2);

        int infoY = linkY + tableHeight + 7;
        graphics.fill(x + PAD, infoY - 3, x + WIDTH - PAD, infoY - 2, COLOR_SEPARATOR);

        for (int i = 0; i < lines.size(); i++) {
            Line line = lines.get(i);
            graphics.drawString(font, line.text(), x + PAD, infoY + i * LINE, line.color(), false);
        }

        int footerY = y + contentHeight - PAD - FOOTER;
        graphics.fill(x + PAD, footerY - 3, x + WIDTH - PAD, footerY - 2, COLOR_SEPARATOR);

        boolean detailHovered = mouseX >= x + 2 && mouseX < x + WIDTH - 2 && mouseY >= footerY - 1 && mouseY < footerY + FOOTER;
        Component detail = Component.translatable("container_loot_tip.detail.label", Component.translatable(settings.detailLevel().translationKey()));

        if (detailHovered) {
            graphics.fill(x + 2, footerY - 1, x + WIDTH - 2, footerY + FOOTER, COLOR_HOVER);
        }

        graphics.drawString(font, detail, x + PAD, footerY + 2, detailHovered ? COLOR_TITLE : COLOR_TEXT, false);
        detailBox = new Box(x + 2, footerY - 1, WIDTH - 4, FOOTER + 1);

        panelBox = new Box(x, y, WIDTH, contentHeight);

        if (linkHovered) {
            Component tooltip = Component.translatable(canLink
                    ? "container_loot_tip.link.tooltip"
                    : "container_loot_tip.link.tooltip.copy");
            graphics.renderTooltip(font, tooltip, mouseX, mouseY);
        }
    }

    private static void renderTitleLink(GuiGraphics graphics, AbstractContainerScreen<?> screen, int mouseX, int mouseY) {
        Font font = Minecraft.getInstance().font;
        Component title = screen.getTitle();
        int textWidth = font.width(title);
        // Vanilla and Lootr screens keep the default title label at (8, 6).
        int x = 8;
        int y = 6;
        boolean canLink = TableOpeners.canOpen();
        boolean hovered = mouseX >= x && mouseX < x + textWidth && mouseY >= y && mouseY < y + font.lineHeight;

        titleLinkBox = new Box(x, y, textWidth, font.lineHeight);

        if (!canLink) {
            return;
        }

        int color = hovered ? COLOR_LINK_HOVER : COLOR_LINK;
        graphics.drawString(font, title, x, y, color, false);
        graphics.fill(x, y + font.lineHeight, x + textWidth, y + font.lineHeight + 1, color);

        if (hovered) {
            graphics.renderTooltip(font, Component.translatable("container_loot_tip.link.tooltip"), mouseX, mouseY);
        }
    }

    private static boolean titleLinkClicked(double mouseX, double mouseY, ContainerLootInfo info) {
        if (titleLinkBox == null || !titleLinkBox.contains(mouseX, mouseY)) {
            return false;
        }

        if (Screen.hasShiftDown()) {
            copyTableId(info);
            return true;
        }

        if (!TableOpeners.canOpen()) {
            return false;
        }

        openTable(info);
        return true;
    }

    private static void openTable(ContainerLootInfo info) {
        if (!TableOpeners.open(info.tableId())) {
            message("container_loot_tip.link.not_found");
        }
    }

    private static void copyTableId(ContainerLootInfo info) {
        Minecraft.getInstance().keyboardHandler.setClipboard(info.tableId().toString());
        message("container_loot_tip.link.copied");
    }

    private static List<Line> buildLines(ContainerLootInfo info, DetailLevel level) {
        List<Line> lines = new ArrayList<>();
        lines.add(new Line(Component.translatable("container_loot_tip.line.source",
                Component.translatable(info.source().translationKey())), COLOR_TEXT));

        if (info.kind() == ContainerKind.OTHER) {
            lines.add(new Line(Component.translatable("container_loot_tip.line.container_unknown"), COLOR_TEXT));
        } else {
            Component kind = Component.translatable(info.kind().translationKey());

            if (info.size() > 0) {
                lines.add(new Line(Component.translatable("container_loot_tip.line.container_size", kind, info.size()), COLOR_TEXT));
            } else {
                lines.add(new Line(Component.translatable("container_loot_tip.line.container", kind), COLOR_TEXT));
            }
        }

        if (level != DetailLevel.DETAILED) {
            return lines;
        }

        if (info.seed() != ContainerLootInfo.UNKNOWN_LONG) {
            Component seed = info.seed() == 0L
                    ? Component.translatable("container_loot_tip.value.random")
                    : Component.literal(Long.toString(info.seed()));
            lines.add(new Line(Component.translatable("container_loot_tip.line.seed", seed), COLOR_TEXT));
        }

        if (info.source() == LootSource.LOOTR) {
            Component opened = Component.translatable(info.openedBefore()
                    ? "container_loot_tip.value.yes"
                    : "container_loot_tip.value.no");
            lines.add(new Line(Component.translatable("container_loot_tip.line.opened", opened), COLOR_TEXT));

            if (info.openers() >= 0) {
                lines.add(new Line(Component.translatable("container_loot_tip.line.openers", info.openers()), COLOR_TEXT));
            }

            if (info.refreshTicks() > 0) {
                lines.add(new Line(Component.translatable("container_loot_tip.line.refresh", duration(info.refreshTicks())), COLOR_TEXT));
            }

            if (info.decayTicks() > 0) {
                lines.add(new Line(Component.translatable("container_loot_tip.line.decay", duration(info.decayTicks())), COLOR_TEXT));
            }
        }

        return lines;
    }

    private static Component duration(int ticks) {
        int seconds = Math.max(1, (ticks + 19) / 20);

        if (seconds >= 60) {
            int minutes = seconds / 60;
            int remainder = seconds % 60;

            if (remainder == 0) {
                return Component.translatable("container_loot_tip.time.minutes", minutes);
            }

            return Component.translatable("container_loot_tip.time.minutes_seconds", minutes, remainder);
        }

        return Component.translatable("container_loot_tip.time.seconds", seconds);
    }

    private static int panelX(AbstractContainerScreen<?> screen, int width, PanelSide side) {
        if (side == PanelSide.RIGHT) {
            int x = screen.getXSize() + 4;

            if (screen.getGuiLeft() + x + width > screen.width - 2) {
                x = screen.width - screen.getGuiLeft() - width - 2;
            }

            return x;
        }

        int x = -width - 4;

        if (screen.getGuiLeft() + x < 2) {
            x = 2 - screen.getGuiLeft();
        }

        return x;
    }

    private static void drawBackground(GuiGraphics graphics, int x, int y, int width, int height) {
        graphics.fill(x, y, x + width, y + height, COLOR_BACKGROUND);
        graphics.fill(x, y, x + width, y + 1, COLOR_BORDER);
        graphics.fill(x, y + height - 1, x + width, y + height, COLOR_BORDER);
        graphics.fill(x, y, x + 1, y + height, COLOR_BORDER);
        graphics.fill(x + width - 1, y, x + width, y + height, COLOR_BORDER);
    }

    private static void message(String key, Object... args) {
        Minecraft minecraft = Minecraft.getInstance();

        if (minecraft.player != null) {
            minecraft.gui.setOverlayMessage(Component.translatable(key, args), false);
        }
    }

    private record Line(Component text, int color) {
    }

    private record Box(int x, int y, int width, int height) {
        private boolean contains(double mouseX, double mouseY) {
            return mouseX >= x && mouseX < x + width && mouseY >= y && mouseY < y + height;
        }
    }
}
