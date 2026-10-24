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
 * <p>Both the collapsed state and the detail level are global rather than per container, matching
 * the requested behaviour: collapsing the panel once keeps it collapsed for every container.</p>
 */
public final class ClientSettings {
    private static final String FILE_NAME = "containerloottip-client.json";
    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();
    private static ClientSettings instance;

    private boolean collapsed;
    private DetailLevel detailLevel = DetailLevel.STANDARD;

    private ClientSettings() {
    }

    public static synchronized ClientSettings get() {
        if (instance == null) {
            instance = load();
        }

        return instance;
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
        private boolean collapsed;
        private String detailLevel;
    }
}
