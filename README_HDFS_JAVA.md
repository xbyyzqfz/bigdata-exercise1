# HDFS文件管理器Java程序使用说明

本文档提供了HDFS文件管理器Java程序的编译和运行指南。

## 项目结构

```
bigdata-exercise1/
├── pom.xml                  # Maven项目配置文件
├── src/
│   ├── main/
│   │   ├── java/com/bigdata/hdfs/
│   │   │   └── HDFSFileManager.java  # 主程序文件
│   │   └── resources/
│   │       └── logback.xml  # 日志配置文件
└── README_HDFS_JAVA.md      # 使用说明
```

## 功能概述

`HDFSFileManager`类实现了以下功能：

- **连接管理**：建立和关闭HDFS连接
- **文件上传**：将本地文件上传到HDFS
- **文件下载**：从HDFS下载文件到本地
- **文件删除**：删除HDFS中的文件或目录
- **目录遍历**：递归列出目录中的所有文件和子目录
- **目录统计**：统计目录中的文件数量、目录数量和总大小

## 环境要求

- JDK 8 或更高版本
- Maven 3.6 或更高版本
- 运行中的Hadoop 3.3.x集群

## 编译和运行步骤

### 1. 编译项目

在项目根目录下执行以下命令：

```bash
mvn clean package
```

这将在`target`目录下生成可执行的JAR文件。

### 2. 运行程序

编译成功后，可以使用以下命令运行程序：

```bash
java -jar target/hdfs-file-manager-1.0.0.jar
```

或者直接运行编译后的类文件：

```bash
mvn exec:java -Dexec.mainClass="com.bigdata.hdfs.HDFSFileManager"
```

### 3. 注意事项

- 确保Hadoop服务已经启动
- 程序默认连接到`hdfs://localhost:9000`，如果您的HDFS服务在不同地址，请修改`main`方法中的`hdfsUri`变量
- 程序运行时会自动创建测试文件和目录，完成测试后会自动清理

## 测试流程说明

主方法中的测试流程包括：

1. 连接到HDFS
2. 创建测试目录
3. 创建本地测试文件并上传到HDFS
4. 遍历目录结构并显示
5. 从HDFS下载文件到本地
6. 统计目录信息
7. 删除测试文件
8. 清理资源并关闭连接

## Web UI验证

运行程序后，可以通过NameNode Web UI验证操作结果：

1. 打开浏览器，访问 http://localhost:9870/
2. 在界面上方导航栏中点击 "Browse the file system"
3. 导航到 `/user/student/test_program` 目录查看测试文件

## 常见问题排查

1. **连接失败**：检查Hadoop服务是否运行，以及URI是否正确
2. **权限错误**：确保当前用户有足够权限访问HDFS目录
3. **网络问题**：检查防火墙设置，确保9000端口开放
4. **依赖问题**：确保Maven已正确下载所有依赖

如有其他问题，请查看程序生成的日志文件`hdfs_manager.log`获取详细信息。