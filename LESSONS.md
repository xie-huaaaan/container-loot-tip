# Lessons

> 上下文压缩后先读本文件，只记录可复用的经验。

## 模组 ID 与批量重命名

- Forge/NeoForge 的 modId 只接受 `[a-z][a-z0-9_]`，短横线会被拒绝；需要 kebab-case 时只改 `base.archivesName`、发布文件名和文档，modId 保持 snake_case。
- 全局替换 `containerloottip.` 会误伤 `dev.containerloottip.*` 包名和 import。重命名语言键时应只匹配带引号的键，资源命名空间用目录重命名处理。

## 界面与配置

- `AbstractContainerScreen` 的标题默认画在 `(8, 6)`，不是居中；覆盖标题做链接时必须用原位置，否则会出现两个标题。
- 读取客户端 JSON 前先剥离 UTF-8 BOM。Windows 编辑器可能自动添加 BOM，Gson 会因此解析失败并回退到默认值。
- 验证配置功能时要使用目标分支实际构建的客户端；旧代码保存配置时会丢弃它不认识的字段，看起来像是配置被自动重置。
