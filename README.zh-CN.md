# Container Loot Tip

[English](README.md) | 简体中文

在带战利品表的容器界面显示战利品表来源与基础信息。与 Advanced Loot Info (ALI) 及 JEI / EMI 同时安装时，可跳转到对应战利品表页面。

## 支持版本

- Minecraft 1.20.1 / Forge
- Minecraft 1.21.1 / NeoForge

## 功能

- 打开带战利品表的容器时，在 GUI 左侧显示可收起的信息面板。
- 显示表名、原版 / Lootr 来源、容器类型与格数；详细度可在精简、标准、详细之间循环切换。
- 表名为超链接：安装 ALI + ACI 以及 JEI 或 EMI 时，点击可精确打开该表的 ALI 页面。
- Lootr 容器额外显示开启人数、刷新 / 衰减等信息（可用时）。
- 面板收起状态和详细度写入客户端配置，全局生效。

## 安装与软依赖

所有联动都是可选的，模组可以在缺少任一联动 mod 的情况下加载：

- 不装 Lootr：原版容器信息仍然工作，但原版战利品表只在容器首次打开时可见。
- 不装 ALI / ACI 或 JEI / EMI：仍显示表名和基础信息，只是表名不能点击跳转。
- 仅客户端安装：模组正常加载，但没有服务端下发的数据，因此不显示面板；不会崩溃。
- 仅服务端安装：不会向未安装本模组的客户端发送任何自定义数据包，这些玩家可以正常进服。

## 限制

- 原版容器在首次打开时就会消耗战利品表。本模组在打开瞬间于服务端捕获并下发，因此只保证首次打开可见；安装本模组之前已经打开过的原版容器无法追溯。
- 表名与状态来自服务端；仅客户端安装时无法显示，这是预期的功能缺失。
- 暂不支持没有 GUI 的容器，也不主动推断非原版 / Lootr 容器的战利品表来源。
- 不计划支持 REI 跳转。

## 可选依赖

| Mod | 作用 |
| --- | --- |
| Lootr | 保留每位玩家的独立战利品，并提供开启人数、刷新 / 衰减等信息 |
| Advanced Loot Info (ALI) + Advanced Core Info (ACI) | 提供战利品表详情页 |
| JEI 或 EMI | 显示 ALI 页面；两者任选其一 |

## 分支

- `main`：共享代码（`src/common`）、文档与规范，不包含可运行的 Gradle 工程。
- `1.20.1-forge`：Minecraft 1.20.1 / Forge 工程。
- `1.21.1-neoforge`：Minecraft 1.21.1 / NeoForge 工程。

## 构建

### 环境

- 1.20.1 分支需要 JDK 17。
- 1.21.1 分支需要 JDK 21。
- 使用仓库自带的 Gradle Wrapper，不需要额外安装 Gradle。
- JDK 和 Gradle 缓存放在仓库之外。请通过 `JAVA_HOME` 或 Gradle 的工具链检测使用本机 JDK，不要把本机路径提交到仓库。

### 命令

Linux / macOS：

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

Windows PowerShell：

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

构建产物位于 `build/libs/`。

### 开发依赖

联动的几个 mod 通过本地 jar 编译，jar 放在仓库内、但被 git 忽略的 `.deps/<branch>/` 目录中。文件名需要与 `gradle.properties` 中的版本属性一致：

1.20.1：

- `lootr-0.7.35.94.jar`
- `ali-2.5.0.jar`
- `aci-1.5.0.jar`
- `jei-15.20.0.105.jar`
- `emi-1.1.18.jar`

1.21.1：

- `lootr-1.11.38.127.jar`
- `ali-2.5.0.jar`
- `aci-1.5.0.jar`
- `jei-19.20.0.242.jar`
- `emi-1.1.22.jar`

这些 jar 只用于编译和开发环境运行；构建出的模组不会打包它们。默认开发客户端加载 JEI，EMI 需要额外加 `-Pwith_emi=true` 才会进入运行时。

### NeoForge Maven 镜像

如果所在网络无法访问 `maven.neoforged.net`，可以临时使用镜像：

```bash
./gradlew build -Pneoforge_mirror=https://neoforged.forgecdn.net
```

### 开发客户端

```bash
./gradlew runClient
```

开发客户端使用项目下的 `run/` 作为游戏目录，而不是官方启动器的 `.minecraft`。日志位于 `run/logs/latest.log`，崩溃报告位于 `run/crash-reports/`。`runClient` 使用离线开发账号，不适合连接需要正版验证的服务器。发布前应使用普通启动器加载 `build/libs/` 中的 jar 再验证一次。

## 许可证

MIT，见 [LICENSE](LICENSE)。
