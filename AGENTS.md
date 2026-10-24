# AGENTS.md

## 项目

Container Loot Tip（mod id `containerloottip`）：在带战利品表的容器界面显示表名、来源和基础信息；表名是超链接，可通过 ALI 在 JEI/EMI 中精确打开对应战利品表页面。

目标版本：Minecraft 1.20.1 Forge、1.21.1 NeoForge。
可选依赖：Lootr（适配）、ALI + ACI（详情跳转）、JEI 或 EMI（查看器）。仅安装 REI 或无查看器时不提供跳转。

**所有联动都是软依赖**：不装 Lootr/ALI/ACI/JEI/EMI 时模组仍可加载，只是对应功能缺失。客户端或服务端单独安装也不能崩溃；服务端单独安装时，未安装本模组的玩家必须能正常进服。

## 分支模型

- `main`：共享代码 `src/common`、文档、规范。不含可运行 Gradle 工程。
- `1.20.1-forge` / `1.21.1-neoforge`：完整工程，只放版本/加载器胶水。
- 共享逻辑先改 `main`，再 merge 到两个版本分支；版本差异不得反向污染 `main`。
- 关键节点提交。提交信息用英文，说明 why。

## 目录

- `src/common/java`、`src/common/resources`：跨版本共享。
- `src/main/java`、`src/main/resources`：版本/加载器专属。
- `refs/`：Lootr、ALI、查看器参考源码（ignored）。
- `.deps/`：本地开发依赖 jar（ignored），布局见 `README.md`。
- `run/`、`build/`、日志：ignored。

## 硬性约定

- `src/common` 必须同时通过 1.20.1 与 1.21.1 编译。不得 import 加载器/版本特有 API，也不得直接 import Lootr/ALI/JEI/EMI/ACI 等可选类。
- 可选依赖必须类加载安全：缺失时不得触发 `NoClassDefFoundError`。先判断 mod 是否加载，再调用隔离的兼容类。
- 服务端权威：表名、种子、开启状态等由服务端在打开菜单时捕获后下发；客户端不得自行推断原版容器表。
- 原版容器只在首次打开时能捕获（表随后被消耗）；Lootr 始终可捕获。
- 网络必须是“可选加入”：`displayTest` 使用 `IGNORE_ALL_VERSION`；发包前确认目标客户端存在本模组的通道，绝不向无模组客户端发送自定义 payload。仅客户端安装时静默不显示，仅服务端安装时对未装模组的玩家无任何影响。
- 模组元数据中的 Lootr/ALI/ACI/JEI/EMI 依赖必须标记为 optional；禁止把它们写成 required。
- 仅 JEI/EMI 支持 ALI 精确跳转。REI-only 或无查看器时，表名降级为普通文本。
- 用户可见文本进 lang（`en_us` + `zh_cn`）；代码标识符用英文。
- 公开文档保持英文与简体中文同步：`README.md` 为英文，`README.zh-CN.md` 为简体中文。
- 类/模块用 Javadoc 说明用途；其余注释只解释“为什么”，只有在功能不明显时才说明“做了什么”。
- 目前不做：开启前预览、非原版/Lootr 容器扩展、REI 跳转。
- 修 bug 后在 `LESSONS.md` 追加可复用经验教训（如果有）。上下文压缩后先读它。
- 不提交 `refs/`、依赖 jar、`run/`、`build/`、IDE 文件。

## 构建与测试

- 1.20.1 分支使用 JDK 17，1.21.1 分支使用 JDK 21；Gradle 缓存放在仓库之外，不在仓库内保存 JDK 或 Gradle 用户目录。
- 不要在提交的配置或文档中写入本机路径；通过 `JAVA_HOME` 或 Gradle 工具链检测选择 JDK。
- 每次改动至少跑对应分支的 `build`，命令见 `README.md`。
- 游戏内实测由维护者执行：完成构建后列出待测项，等待维护者提供截图或描述，不启动客户端做 UI 自动化。
- 关键手动检查：原版首次打开/重开、Lootr 箱子/木桶/潜影盒、三档详细度、收起持久化、JEI/EMI 跳转、ALI 缺失、仅 REI。
- 安装组合检查：仅客户端、仅服务端（原版客户端进服）、双端、双端且缺少各个可选 mod。服务端只装本模组时绝不能因发包导致客户端断线。
- 发布前用普通启动器加载 `build/libs/` 中的 jar，按正常玩家环境复测。

## 协作

- 遇到需要人类决策或必须人工进游戏测试的问题，直接问，不硬猜。
