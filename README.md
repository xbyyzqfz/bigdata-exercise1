# HDFS 常见操作练习

本文件包含练习一中所有要求的HDFS命令，可在已配置好的Hadoop 3.3.x环境中直接运行。

## 第一部分：基础操作评估

### 任务 1：目录和文件管理

#### 1.1 目录结构创建

```bash
# 创建主目录和子目录
hdfs dfs -mkdir -p /user/student/project/input
hdfs dfs -mkdir -p /user/student/project/output
hdfs dfs -mkdir -p /user/student/project/temp

# 或者分步骤创建
# 命令 1：创建项目主目录
hdfs dfs -mkdir -p /user/student/project/
# 命令 2：创建输入目录
hdfs dfs -mkdir -p /user/student/project/input/
# 命令 3：创建输出和临时目录
hdfs dfs -mkdir -p /user/student/project/output/
hdfs dfs -mkdir -p /user/student/project/temp/
```

#### 1.2 文件上传和管理

```bash
# 本地文件创建：创建一个包含100行数据的测试文件
echo "创建测试文件 test.txt..."
for i in {1..100}; do
echo "这是测试文件的第 $i 行数据，用于HDFS练习">> test.txt
done

# 文件上传：将本地文件上传到HDFS
hdfs dfs -put test.txt /user/student/project/input/

# 查看文件头部：查看文件的前10行
hdfs dfs -cat /user/student/project/input/test.txt | head -10

# 查看文件尾部：查看文件的最后10行
hdfs dfs -cat /user/student/project/input/test.txt | tail -10

# 查看文件属性：查看文件的详细信息
hdfs dfs -ls -h /user/student/project/input/test.txt
# 或者使用更详细的命令
hdfs dfs -stat "%o %b %n %u %g %a %y" /user/student/project/input/test.txt
```

#### 1.3 文件操作和权限管理

```bash
# 文件复制：将input目录中的文件复制到temp目录
hdfs dfs -cp /user/student/project/input/test.txt /user/student/project/temp/

# 文件权限修改：将temp目录中文件的权限设置为644
hdfs dfs -chmod 644 /user/student/project/temp/test.txt

# 目录权限修改：将temp目录的权限设置为755
hdfs dfs -chmod 755 /user/student/project/temp/

# 权限验证：检查权限设置是否正确
hdfs dfs -ls -la /user/student/project/temp/
```

### 任务 2：批量操作

#### 2.1 批量文件上传

```bash
# 批量文件创建：在本地创建5个不同的文件
for i in {1..5}; do
echo "创建文件 file$i.txt..."
for j in {1..20}; do
echo "这是文件 file$i.txt 的第 $j 行数据">> file$i.txt
done
done

# 批量上传：将所有文件上传到HDFS
hdfs dfs -put file*.txt /user/student/project/input/

# 验证上传结果：使用通配符验证所有文件都已上传成功
hdfs dfs -ls /user/student/project/input/file*.txt
```

#### 2.2 通配符操作

```bash
# 通配符列出文件：列出所有.txt文件
hdfs dfs -ls /user/student/project/input/*.txt

# 通配符复制文件：复制所有以"file"开头的文件到temp目录
hdfs dfs -cp /user/student/project/input/file*.txt /user/student/project/temp/

# 统计文件数量：统计input目录中文件的总数量
hdfs dfs -ls /user/student/project/input/ | wc -l
# 或者更准确的统计（排除目录行）
hdfs dfs -ls /user/student/project/input/ | grep -v "^d" | wc -l
```

#### 2.3 目录操作和清理

```bash
# 创建备份目录
hdfs dfs -mkdir -p /user/student/backup/

# 复制整个目录：将整个project目录复制到backup目录
hdfs dfs -cp -r /user/student/project/ /user/student/backup/

# 清理temp目录：删除temp目录中的所有文件（保留目录）
hdfs dfs -rm /user/student/project/temp/*

# 验证结果：检查各目录状态
hdfs dfs -ls -R /user/student/backup/
hdfs dfs -ls /user/student/project/temp/
```

## Web UI 验证说明

在执行完上述命令后，可以通过以下方式在NameNode Web UI中验证结果：

1. 打开浏览器，访问 http://localhost:9870/
2. 在界面上方导航栏中点击 "Browse the file system"
3. 依次导航到相应目录，检查目录结构和文件是否正确创建
4. 点击文件名可以查看文件详细信息和权限设置

## 注意事项

1. 确保Hadoop服务已经启动：
   ```bash
   start-dfs.sh  # Linux/Mac
   # 或在Windows上使用相应的启动脚本
   ```

2. 如果遇到权限问题，可以使用管理员权限运行命令或修改相关配置

3. 完成练习后，可以停止Hadoop服务：
   ```bash
   stop-dfs.sh  # Linux/Mac
   # 或在Windows上使用相应的停止脚本
   ```