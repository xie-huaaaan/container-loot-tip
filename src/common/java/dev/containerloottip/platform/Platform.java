package dev.containerloottip.platform;

import java.nio.file.Path;

/** Loader-specific services needed by shared code. */
public interface Platform {
    boolean isModLoaded(String modId);

    Path configDir();
}
