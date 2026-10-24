package dev.containerloottip.platform;

/** Holds the platform implementation installed by the loader entrypoint. */
public final class PlatformHolder {
    private static Platform instance;

    private PlatformHolder() {
    }

    public static void set(Platform platform) {
        if (instance != null) {
            throw new IllegalStateException("Platform already initialized");
        }
        instance = platform;
    }

    public static boolean isInitialized() {
        return instance != null;
    }

    public static Platform get() {
        Platform platform = instance;
        if (platform == null) {
            throw new IllegalStateException("Platform not initialized");
        }
        return platform;
    }
}
