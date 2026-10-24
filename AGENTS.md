# AGENTS.md

## 项目

Container Loot Tip（工作名，mod id `containerloottip`）：在带战利品表的容器界面显示表名、来源和基础信息；表名是超链接，可通过 ALI 在 JEI/EMI 中精确打开对应战利品表页面。

目标版本：Minecraft 1.20.1 Forge、1.21.1 NeoForge。
可选依赖：Lootr（适配）、ALI + ACI（详情跳转）、JEI 或 EMI（查看器）。仅安装 REI 或无查看器时不提供跳转。

## 分支模型

- `main`：共享代码 `src/common`、文档、规范。不含可运行 Gradle 工程。
- `1.20.1-forge` / `1.21.1-neoforge`：完整工程，只放版本/加载器胶水。
- 共享逻辑先改 `main`，再 merge 到两个版本分支；版本差异不得反向污染 `main`。
- 关键节点提交。提交信息用英文，说明 why。

## 目录

- `src/common/java`、`src/common/resources`：跨版本共享。
- `src/main/java`、`src/main/resources`：版本/加载器专属。
- `refs/`：Lootr、ALI、查看器参考源码（ignored）。
- `.jdks/`、`.gradle-home/`、`.deps/`：工作区内的 JDK、Gradle 缓存、依赖 jar（ignored）。
- `run/`、`build/`、日志：ignored。

## 硬性约定

- `src/common` 必须同时通过 1.20.1 与 1.21.1 编译。不得 import 加载器/版本特有 API，也不得直接 import Lootr/ALI/JEI/EMI/ACI 等可选类。
- 可选依赖必须类加载安全：缺失时不得触发 `NoClassDefFoundError`。先判断 mod 是否加载，再调用隔离的兼容类。
- 服务端权威：表名、种子、开启状态等由服务端在打开菜单时捕获后下发；客户端不得自行推断原版容器表。
- 原版容器只在首次打开时能捕获（表随后被消耗）；Lootr 始终可捕获。服务器必须安装本模组。
- 仅 JEI/EMI 支持 ALI 精确跳转。REI-only 或无查看器时，表名降级为普通文本。
- 用户可见文本进 lang（`en_us` + `zh_cn`）；代码标识符用英文。
- 类/模块用 Javadoc 说明用途；行内注释只解释“为什么”，不写显而易见的“做了什么”。
- v1 不做：开启前预览、非原版/Lootr 容器扩展、REI 跳转。
- 修 bug 后在 `LESSONS.md` 追加 1-3 行经验（现象 → 根因 → 结论）。上下文压缩后先读它。
- 不提交 `refs/`、依赖 jar、`run/`、`build/`、IDE 文件。

## 构建与测试

- 使用工作区内 JDK 和项目级 Gradle 缓存；命令见 `README.md`。
- 每次改动至少跑对应分支的 `build`。
- UI 改动必须实际进游戏验证，不能只凭编译通过。
- 关键手动检查：原版首次打开/重开、Lootr 箱子/木桶/潜影盒、三档详细度、收起持久化、JEI/EMI 跳转、ALI 缺失、仅 REI。

## 协作

- 用户没有 Java 开发经验：解释重大设计和风险，不要只贴代码。
- 遇到需要人类决策或必须人工进游戏测试的问题，直接问，不硬猜。
