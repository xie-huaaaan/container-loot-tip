# Lessons

> 上下文压缩后先读本文件，只记录可复用的经验。

## 模组 ID 与批量重命名

- Forge/NeoForge 的 modId 只接受 `[a-z][a-z0-9_]`，短横线会被拒绝；需要 kebab-case 时只改 `base.archivesName`、发布文件名和文档，modId 保持 snake_case。
- 全局替换 `containerloottip.` 会误伤 `dev.containerloottip.*` 包名和 import。重命名语言键时应只匹配带引号的键，资源命名空间用目录重命名处理。
