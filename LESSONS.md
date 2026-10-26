# Lessons

> 上下文压缩后先读本文件，只记录可复用的经验。

## 模组 ID 与批量重命名

- Forge/NeoForge 的 modId 只接受 `[a-z][a-z0-9_]`，短横线会被拒绝；需要 kebab-case 时只改 `base.archivesName`、发布文件名和文档，modId 保持 snake_case。
- 全局替换 `containerloottip.` 会误伤 `dev.containerloottip.*` 包名和 import。重命名语言键时应只匹配带引号的键，资源命名空间用目录重命名处理。

## 界面与配置

- 覆盖容器标题时不要假设它居中，应读取 `titleLabelX/Y`。两个分支都要通过 AT 暴露字段：Forge 1.20.1 自动加载 `META-INF/accesstransformer.cfg`，NeoForge 1.21.1 还需要在 `neoforge.mods.toml` 声明 `[[accessTransformers]]`。
- 读取客户端 JSON 前先剥离 UTF-8 BOM。Windows 编辑器可能自动添加 BOM，Gson 会因此解析失败并回退到默认值。
- 验证配置功能时要使用目标分支实际构建的客户端；旧代码保存配置时会丢弃它不认识的字段，看起来像是配置被自动重置。
