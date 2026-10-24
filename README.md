# Container Loot Tip

English | [简体中文](README.zh-CN.md)

Shows the source loot table and basic information in the GUI of containers that use loot tables. When Advanced Loot Info (ALI) and JEI or EMI are installed, the table name becomes a link to the matching ALI page.

## Supported versions

- Minecraft 1.20.1 / Forge
- Minecraft 1.21.1 / NeoForge

## Features

- Shows a collapsible info panel on the left side of container screens that use loot tables.
- Displays the table ID, the vanilla / Lootr source, container type and slot count. The detail level cycles between Compact, Standard and Detailed.
- The table ID is a hyperlink when ALI + ACI and JEI or EMI are installed. Clicking it opens the exact ALI page for that table.
- Lootr containers additionally show opener counts, refresh and decay information when available.
- The collapsed state and detail level are saved to the client config and apply globally.

## Installation and optional dependencies

Every integration is optional. The mod loads without any of the supported companion mods:

- Without Lootr: vanilla containers still show their table, but a vanilla loot table is only visible when the container is opened for the first time.
- Without ALI / ACI or JEI / EMI: the table ID and basic information are still shown, but the ID cannot be clicked.
- Client only: the mod loads normally, but no server data is available, so no panel is shown. Nothing crashes.
- Server only: the mod never sends a custom payload to clients that do not have the mod, so vanilla clients can join normally.

## Limitations

- Vanilla containers consume their loot table when opened for the first time. The mod captures and sends the table at that moment, so it can only be shown on the first open. Containers opened before the mod was installed cannot be recovered.
- Table data comes from the server. Client-only installations cannot show the panel; this is expected.
- Containers without a GUI are not supported, and the mod does not try to infer loot tables for non-vanilla / non-Lootr containers.
- REI navigation is not planned.

## Optional dependencies

| Mod | Purpose |
| --- | --- |
| Lootr | Keeps per-player loot and provides opener, refresh and decay information |
| Advanced Loot Info (ALI) + Advanced Core Info (ACI) | Provides the loot table detail pages |
| JEI or EMI | Displays ALI pages; either one is sufficient |

## Branches

- `main`: shared code (`src/common`), documentation and conventions. Does not contain a runnable Gradle project.
- `1.20.1-forge`: Minecraft 1.20.1 / Forge project.
- `1.21.1-neoforge`: Minecraft 1.21.1 / NeoForge project.

## Building

### Requirements

- JDK 17 for the 1.20.1 branch.
- JDK 21 for the 1.21.1 branch.
- Use the Gradle Wrapper included in the repository; no separate Gradle installation is needed.
- Keep JDKs and the Gradle cache outside the repository. Point `JAVA_HOME` at your JDK or let Gradle toolchain detection find it. Do not commit machine-specific paths.

### Commands

Linux / macOS:

```bash
git checkout 1.20.1-forge
export JAVA_HOME=/path/to/jdk-17
./gradlew build
```

```bash
git checkout 1.21.1-neoforge
export JAVA_HOME=/path/to/jdk-21
./gradlew build
```

Windows PowerShell:

```powershell
git checkout 1.20.1-forge
$env:JAVA_HOME = "C:\path\to\jdk-17"
.\gradlew.bat build
```

```powershell
git checkout 1.21.1-neoforge
$env:JAVA_HOME = "C:\path\to\jdk-21"
.\gradlew.bat build
```

Build artifacts are written to `build/libs/`.

### Development dependencies

The companion mods are compiled against local jars placed in `.deps/<branch>/`, which is ignored by git. File names must match the version properties in `gradle.properties`:

1.20.1:

- `lootr-0.7.35.94.jar`
- `ali-2.5.0.jar`
- `aci-1.5.0.jar`
- `jei-15.20.0.105.jar`
- `emi-1.1.18.jar`

1.21.1:

- `lootr-1.11.38.127.jar`
- `ali-2.5.0.jar`
- `aci-1.5.0.jar`
- `jei-19.20.0.242.jar`
- `emi-1.1.22.jar`

These jars are only used for compilation and the development runtime; they are not bundled into the built mod. Download the files from each mod's release page, using the versions listed above. The development client loads JEI by default. Add `-Pwith_emi=true` to load EMI alongside it.

### NeoForge Maven mirror

If `maven.neoforged.net` is unreachable on your network, use the mirror for a build:

```bash
./gradlew build -Pneoforge_mirror=https://neoforged.forgecdn.net
```

### Development client

```bash
./gradlew runClient
```

The development client uses the project-local `run/` directory instead of the launcher's `.minecraft`. Logs are written to `run/logs/latest.log` and crash reports to `run/crash-reports/`. `runClient` uses an offline development account and is not suitable for servers that require authentication. Before a release, load the jar from `build/libs/` in a normal launcher and test it once more.

## License

MIT. See [LICENSE](LICENSE).
