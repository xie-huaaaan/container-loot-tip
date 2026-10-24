# Container Loot Tip

在带战利品表的容器界面显示战利品表来源与基础信息，并通过 Advanced Loot Info (ALI) 精确跳转到对应战利品表页面。

## 版本

- Minecraft 1.20.1 / Forge
- Minecraft 1.21.1 / NeoForge

## 功能

- 打开带战利品表的容器时，在 GUI 左侧显示可收起的信息面板。
- 显示表名、原版/Lootr 来源、容器类型与格数；详细度可在精简/标准/详细之间循环切换。
- 表名为超链接：安装 ALI + ACI 和 JEI 或 EMI 时，点击精确打开该表的 ALI 页面。
- Lootr 容器额外显示每位玩家独立、开启人数、刷新/衰减等信息（可用时）。
- 面板收起状态和详细度写入客户端配置，全局生效。

## 软依赖与安装方式

所有联动都是可选的：

- 不装 Lootr：原版容器信息仍然工作。
- 不装 ALI/ACI 或 JEI/EMI：仍显示表名和基础信息，只是表名不能点击跳转。
- 仅客户端安装：模组正常加载，但没有服务端下发的数据，因此不显示面板；不会崩溃。
- 仅服务端安装：不会向未安装本模组的客户端发送任何自定义数据包，这些玩家可以正常进服；装了本模组的客户端会正常看到面板。

## 限制

- 原版容器在首次打开时就会消耗战利品表。本模组在打开瞬间于服务端捕获并下发，因此只能保证首次打开可见；模组安装前或服务器重启前已打开过的原版容器无法追溯。
- 表名与状态来自服务端；仅客户端安装时无法显示（属于预期的功能缺失）。
- 仅安装 REI 或没有 JEI/EMI 时不提供 ALI 跳转，基础信息仍然显示。
- v1 不做开启前准星预览，也不主动支持非原版/Lootr 容器的表来源推断。

## 可选依赖

- Lootr：1.20.1 Forge / 1.21.1 NeoForge
- Advanced Loot Info (ALI) + Advanced Core Info (ACI)
- JEI 或 EMI（ALI 跳转需要其一）

## 分支

- `main`：共享代码与文档。
- `1.20.1-forge`：Forge 1.20.1 工程。
- `1.21.1-neoforge`：NeoForge 1.21.1 工程。

## 构建

本机已安装的 Zulu JDK 即可，不需要把 JDK 复制到工作区。

1.20.1 Forge：

```bash
git checkout 1.20.1-forge
JAVA_HOME="C:/Program Files/Zulu/zulu-17" ./gradlew build
```

1.21.1 NeoForge：

```bash
git checkout 1.21.1-neoforge
JAVA_HOME="C:/Program Files/Zulu/zulu-21" ./gradlew build
```

构建产物在 `build/libs/`。开发环境启动客户端用 `./gradlew runClient`，但游戏内实测由用户执行。

NeoForge 构建使用 `https://neoforged.forgecdn.net/` 镜像，因为部分网络无法访问官方 `maven.neoforged.net`。
