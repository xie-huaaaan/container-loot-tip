package dev.containerloottip.config;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import dev.containerloottip.ContainerLootTip;
import dev.containerloottip.platform.PlatformHolder;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;

/**
 * Global client preferences persisted next to the other mod configs.
 *
 * <p>All preferences are global rather than per container. The file is the only way to change the
 * panel mode and side, so changes take effect after the client restarts.</p>
 */
public final class ClientSettings {
    private static final String FILE_NAME = "container_loot_tip-client.json";
    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();
    private static ClientSettings instance;

    private PanelMode panelMode = PanelMode.SIDEBAR;
    private PanelSide panelSide = PanelSide.LEFT;
    private boolean collapsed;
    private DetailLevel detailLevel = DetailLevel.COMPACT;

    private ClientSettings() {
    }

    public static synchronized ClientSettings get() {
        if (instance == null) {
            instance = load();
        }

        return instance;
    }

    public PanelMode panelMode() {
        return panelMode;
    }

    public PanelSide panelSide() {
        return panelSide;
    }

    public boolean isCollapsed() {
        return collapsed;
    }

    public DetailLevel detailLevel() {
        return detailLevel;
    }

    public void setCollapsed(boolean collapsed) {
        this.collapsed = collapsed;
        save();
    }

    public void cycleDetailLevel() {
        detailLevel = detailLevel.next();
        save();
    }

    private static ClientSettings load() {
        ClientSettings settings = new ClientSettings();
        Path path = settingsPath();

        if (path == null || !Files.isRegularFile(path)) {
            return settings;
        }

        try {
            String json = Files.readString(path, StandardCharsets.UTF_8);
            Data data = GSON.fromJson(json, Data.class);

            if (data != null) {
                settings.panelMode = PanelMode.byName(data.panelMode);
                settings.panelSide = PanelSide.byName(data.panelSide);
                settings.collapsed = data.collapsed;
                settings.detailLevel = DetailLevel.byName(data.detailLevel);
            }
        } catch (Exception e) {
            ContainerLootTip.LOGGER.warn("Unable to read client settings from {}", path, e);
        }

        return settings;
    }

    private void save() {
        Path path = settingsPath();

        if (path == null) {
            return;
        }

        Data data = new Data();
        data.panelMode = panelMode.name();
        data.panelSide = panelSide.name();
        data.collapsed = collapsed;
        data.detailLevel = detailLevel.name();

        try {
            if (path.getParent() != null) {
                Files.createDirectories(path.getParent());
            }

            Path temporary = path.resolveSibling(path.getFileName() + ".tmp");
            Files.writeString(temporary, GSON.toJson(data), StandardCharsets.UTF_8);
            Files.move(temporary, path, StandardCopyOption.REPLACE_EXISTING);
        } catch (IOException e) {
            ContainerLootTip.LOGGER.warn("Unable to save client settings to {}", path, e);
        }
    }

    private static Path settingsPath() {
        if (!PlatformHolder.isInitialized()) {
            return null;
        }

        return PlatformHolder.get().configDir().resolve(FILE_NAME);
    }

    private static final class Data {
        private String panelMode;
        private String panelSide;
        private boolean collapsed;
        private String detailLevel;
    }
}
