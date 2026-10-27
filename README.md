# Container Loot Tip

English | [简体中文](README.zh-CN.md)

[![License: MIT](https://img.shields.io/badge/License-MIT-yellow.svg)](LICENSE) [![Minecraft](https://img.shields.io/badge/Minecraft-1.20.1%20%7C%201.21.1-green.svg)](#) [![Loader](https://img.shields.io/badge/Loader-Forge%20%7C%20NeoForge-orange.svg)](#)

Displays the source loot table and basic container info beside container GUIs, or as a link on the container title. When installed alongside **Advanced Loot Info (ALI)** and **JEI / EMI**, the loot table ID becomes a clickable link that opens the corresponding ALI page directly.

![Sidebar mode](docs/assets/preview.png)

![Title-link mode](docs/assets/preview1.png)

## ✨ Features

- **Collapsible Info Panel**: Adds a **toggleable** panel to the left or right side of container screens that use loot tables.
- **Detailed Metadata**: Displays the loot table ID, source (`Vanilla` / `Lootr`), container type, and slot count. Easily toggle between **Compact** and **Detailed** views.
- **Title-Link Mode**: Can hide the panel entirely and turn the container title into the link instead.
- **One-Click Lookup**: When ALI + ACI and JEI or EMI are present, clicking the loot table ID or title opens its exact loot table page in JEI/EMI.
- **Copy Table ID**: Shift-click the loot table ID or title to copy it to the clipboard.
- **Lootr Integration**: Displays opener counts, refresh and decay timers for Lootr containers when available.
- **Persistent Preferences**: Panel mode, side, collapse state and detail level are saved globally and can be changed through the config file or client commands.

## 📦 Installation & Compatibility

- **Minecraft 1.20.1** — Forge
- **Minecraft 1.21.1** — NeoForge

### Environment Support

| Environment | Behavior |
| --- | --- |
| **Client + Server** | **Recommended**. Full functionality enabled. |
| **Server-Only** | Compatible. Custom payloads are only sent to clients with the mod installed; vanilla clients can join normally. |
| **Client-Only** | Inert. The mod loads safely without crashing, but the panel will not appear since loot table data must be sent by the server. |

### Optional Dependencies

All integrations are completely optional. The mod runs standalone and enables features dynamically based on installed mods:

| Mod | Purpose | Behavior When Missing |
| --- | --- | --- |
| **Lootr** | Preserves per-player loot and provides opener, refresh, and decay info | Vanilla containers still work, but tables are only visible on first open |
| **ALI + ACI** | Provides loot table detail pages (*Advanced Loot Info* & *Advanced Core Info*) | Table ID and basic info are shown, but the ID is not clickable |
| **JEI or EMI** | Displays ALI pages (either one works; REI is not planned) | Same as above; table ID is not clickable |

## ⚙️ Configuration

Client preferences live in `config/container_loot_tip-client.json`. There is no in-game settings screen, but the file is watched automatically and can also be changed with client commands.

```json
{
  "panelMode": "SIDEBAR",
  "panelSide": "LEFT",
  "collapsed": false,
  "detailLevel": "COMPACT"
}
```

| Key | Values | Default | Description |
| --- | --- | --- | --- |
| `panelMode` | `SIDEBAR`, `TITLE_LINK` | `SIDEBAR` | Full sidebar, or only a link on the container title |
| `panelSide` | `LEFT`, `RIGHT` | `LEFT` | Sidebar side; ignored in `TITLE_LINK` mode |
| `collapsed` | `true`, `false` | `false` | Sidebar collapse state; ignored in `TITLE_LINK` mode |
| `detailLevel` | `COMPACT`, `DETAILED` | `COMPACT` | Amount of info shown in the sidebar |

Values are case-insensitive. Missing or invalid values fall back to the defaults. The file must be valid JSON; comments are not supported. It is created the first time the mod saves a preference.

The config is checked about once per second while the game is running, so external edits are applied without a restart. A manual reload command is intentionally not provided.

| Command | Description |
| --- | --- |
| `/container_loot_tip mode <SIDEBAR\|TITLE_LINK>` | Switch between the sidebar and title-link modes |
| `/container_loot_tip side <LEFT\|RIGHT>` | Move the sidebar |
| `/container_loot_tip detail <COMPACT\|DETAILED>` | Change the sidebar detail level |
| `/container_loot_tip collapse <true\|false>` | Collapse or expand the sidebar |

Commands save the whole setting back to the same JSON file. The in-game panel buttons can also change `collapsed` and `detailLevel`. In `TITLE_LINK` mode there is no sidebar; if ALI and JEI/EMI are not installed, the screen looks unchanged.

## ⚠️ Known Limitations

- **Vanilla Container Consumption**: Vanilla Minecraft consumes and clears a container's loot table tag the moment it is opened for the first time. Because this mod captures the table on the server during that initial open event, **vanilla containers only show the panel the very first time they are opened** (containers opened prior to installing the mod cannot be recovered). Use **Lootr** if you want persistent loot table info.
- **Supported Containers**: Containers without a GUI are not supported, and the mod does not attempt to infer loot tables for custom containers outside of Vanilla and Lootr.

## 🛠️ Building & Development

### Branches

- `main`: Shared code (`src/common`), documentation, and conventions (not a runnable Gradle project).
- `1.20.1-forge`: Minecraft 1.20.1 Forge project (requires **JDK 17**).
- `1.21.1-neoforge`: Minecraft 1.21.1 NeoForge project (requires **JDK 21**).

### Local Development Dependencies

Companion mods are compiled against local jars placed in `.deps/<branch>/` (git-ignored). Download the required versions matching the properties in that branch's `gradle.properties`:

- `lootr`, `ali`, `aci`, `jei`, `emi` (used strictly for compilation and dev runtime; never bundled).

### Gradle Commands

Check out the target branch (`1.20.1-forge` or `1.21.1-neoforge`) and run:

```bash
# Build the mod (artifacts output to build/libs/)
./gradlew build

# Launch dev client (loads JEI by default; append -Pwith_emi=true to also load EMI)
./gradlew runClient

# Build using the NeoForge mirror if maven.neoforged.net is unreachable
./gradlew build -Pneoforge_mirror=[https://neoforged.forgecdn.net](https://neoforged.forgecdn.net)
```

## 📄 License

Licensed under the [MIT](LICENSE) License.
