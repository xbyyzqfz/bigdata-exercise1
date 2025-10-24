# GitHub 提交指南

本指南将帮助您将本地项目提交到 GitHub 仓库。

## 前提条件

- 已安装 Git（可通过 `git --version` 检查）
- 已在 GitHub 上创建了目标仓库
- 已配置好 Git 用户信息

## 步骤 1：初始化 Git 仓库（如果尚未初始化）

如果当前目录还不是 Git 仓库，请执行以下命令：

```bash
# 进入项目目录
cd d:\NJU\bigdata-exercise1

# 初始化 Git 仓库
git init
```

## 步骤 2：配置 Git 用户信息

```bash
# 配置用户名
git config user.name "您的GitHub用户名"

# 配置邮箱
git config user.email "您的GitHub邮箱"
```

## 步骤 3：创建 .gitignore 文件（推荐）

创建一个 `.gitignore` 文件来忽略不需要提交的文件：

```bash
echo "# IDE 文件\n.idea/\n.vscode/\n*.swp\n*.swo\n*~\n\n# 编译输出\ntarget/\nbuild/\n*.class\n*.jar\n\n# 日志文件\n*.log\n\n# 操作系统文件\n.DS_Store\nThumbs.db\n\n# 环境变量文件\n.env\n*.properties" > .gitignore
```

## 步骤 4：添加文件到暂存区

```bash
# 添加所有文件（除了.gitignore中指定的）
git add .

# 或者添加特定文件
git add pom.xml src/ README.md README_HDFS_JAVA.md
```

## 步骤 5：提交更改

```bash
git commit -m "完成HDFS常见操作练习：命令行操作和Java SDK开发"
```

## 步骤 6：关联远程仓库

```bash
# 添加远程仓库（请替换为您的GitHub仓库URL）
git remote add origin https://github.com/您的用户名/您的仓库名.git
```

## 步骤 7：推送到远程仓库

```bash
# 推送到main分支（如果是第一次推送，需要使用-u参数设置上游分支）
git push -u origin main

# 后续推送可以简化为
git push
```

## 如果遇到身份验证问题

如果您使用的是较新版本的 Git，可能需要使用个人访问令牌（PAT）进行身份验证：

1. 访问 GitHub -> Settings -> Developer settings -> Personal access tokens
2. 生成一个新的访问令牌，选择适当的权限（至少需要 repo 权限）
3. 在推送时，用户名使用您的 GitHub 用户名，密码使用生成的访问令牌

## Windows 系统注意事项

- 在 Windows 上使用 Git Bash 或 PowerShell 执行上述命令
- 确保路径分隔符正确（Windows 中可以使用 `/` 或 `\`）
- 如果遇到行尾符号问题，可以执行：
  ```bash
  git config core.autocrlf true
  ```

## 验证提交是否成功

1. 打开您的 GitHub 仓库页面
2. 检查是否能看到刚刚提交的文件和提交信息
3. 确认文件结构是否正确

## 常见问题排查

- **推送被拒绝**：可能是因为远程仓库有您本地没有的提交，先执行 `git pull --rebase` 然后再推送
- **权限错误**：检查远程仓库 URL 和访问令牌是否正确
- **文件太大**：GitHub 对单个文件大小有限制（通常是 100MB），可以使用 Git LFS 或考虑不提交大型文件

按照以上步骤操作，您应该能够成功将项目提交到 GitHub 仓库。