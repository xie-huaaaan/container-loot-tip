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
 * Global client preferences persisted in the client config.
 *
 * <p>Once loaded, the file is checked periodically so external edits are picked up without a
 * restart. In-game mutations reload the file first, preserving unrelated manual changes.</p>
 */
public final class ClientSettings {
    private static final String FILE_NAME = "container_loot_tip-client.json";
    private static final int RELOAD_INTERVAL_TICKS = 20;
    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();
    private static ClientSettings instance;
    private static int reloadTicks = RELOAD_INTERVAL_TICKS;

    private PanelMode panelMode = PanelMode.SIDEBAR;
    private PanelSide panelSide = PanelSide.LEFT;
    private boolean collapsed;
    private DetailLevel detailLevel = DetailLevel.COMPACT;
    private long lastModified;

    private ClientSettings() {
    }

    public static synchronized ClientSettings get() {
        if (instance == null) {
            instance = load();
            reloadTicks = RELOAD_INTERVAL_TICKS;
        }

        return instance;
    }

    /** Called from the client tick loop to detect external config changes. */
    public static synchronized void tick() {
        if (instance == null) {
            return;
        }

        if (reloadTicks > 0) {
            reloadTicks--;
            return;
        }

        reloadTicks = RELOAD_INTERVAL_TICKS;
        instance.reloadIfChanged();
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

    public synchronized void setPanelMode(PanelMode panelMode) {
        reloadIfChanged();
        this.panelMode = panelMode;
        save();
    }

    public synchronized void setPanelSide(PanelSide panelSide) {
        reloadIfChanged();
        this.panelSide = panelSide;
        save();
    }

    public synchronized void setCollapsed(boolean collapsed) {
        reloadIfChanged();
        this.collapsed = collapsed;
        save();
    }

    public synchronized void setDetailLevel(DetailLevel detailLevel) {
        reloadIfChanged();
        this.detailLevel = detailLevel;
        save();
    }

    public synchronized void cycleDetailLevel() {
        reloadIfChanged();
        detailLevel = detailLevel.next();
        save();
    }

    public synchronized void reloadIfChanged() {
        Path path = settingsPath();

        if (path == null || !Files.isRegularFile(path)) {
            return;
        }

        long modified = modifiedTime(path);

        if (modified == lastModified) {
            return;
        }

        try {
            apply(read(path));
        } catch (Exception e) {
            ContainerLootTip.LOGGER.warn("Unable to reload client settings from {}", path, e);
        }

        lastModified = modified;
    }

    private static ClientSettings load() {
        ClientSettings settings = new ClientSettings();
        Path path = settingsPath();
        settings.lastModified = modifiedTime(path);

        try {
            settings.apply(read(path));
        } catch (Exception e) {
            ContainerLootTip.LOGGER.warn("Unable to read client settings from {}", path, e);
        }

        return settings;
    }

    private void apply(Data data) {
        if (data == null) {
            return;
        }

        panelMode = PanelMode.byName(data.panelMode);
        panelSide = PanelSide.byName(data.panelSide);
        collapsed = data.collapsed;
        detailLevel = DetailLevel.byName(data.detailLevel);
    }

    private static Data read(Path path) throws IOException {
        if (path == null || !Files.isRegularFile(path)) {
            return null;
        }

        String json = Files.readString(path, StandardCharsets.UTF_8);

        // Some Windows editors prepend a UTF-8 BOM, which Gson rejects.
        if (!json.isEmpty() && json.charAt(0) == '\uFEFF') {
            json = json.substring(1);
        }

        return GSON.fromJson(json, Data.class);
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
            lastModified = modifiedTime(path);
        } catch (IOException e) {
            ContainerLootTip.LOGGER.warn("Unable to save client settings to {}", path, e);
        }
    }

    private static long modifiedTime(Path path) {
        if (path == null || !Files.isRegularFile(path)) {
            return 0L;
        }

        try {
            return Files.getLastModifiedTime(path).toMillis();
        } catch (IOException e) {
            return 0L;
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
