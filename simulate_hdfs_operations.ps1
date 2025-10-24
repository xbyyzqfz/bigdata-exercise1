# 模拟HDFS操作脚本
# 此脚本用于展示HDFS命令的执行过程和预期输出

Write-Output "=" * 60
Write-Output "         HDFS 操作模拟报告          "
Write-Output "=" * 60

# 模拟目录结构创建
Write-Output "\n[任务 1.1] 目录结构创建"
Write-Output "-" * 60
Write-Output "执行命令: hdfs dfs -mkdir -p /user/student/project/input"
Write-Output "执行命令: hdfs dfs -mkdir -p /user/student/project/output"
Write-Output "执行命令: hdfs dfs -mkdir -p /user/student/project/temp"
Write-Output "\n目录结构创建成功!"
Write-Output "预期目录结构:"
Write-Output "/user/student/project/"
Write-Output "├── input/"
Write-Output "├── output/"
Write-Output "└── temp/"

# 模拟文件创建和上传
Write-Output "\n[任务 1.2] 文件上传和管理"
Write-Output "-" * 60
Write-Output "执行命令: 创建本地测试文件 test.txt (100行数据)"
Write-Output "执行命令: hdfs dfs -put test.txt /user/student/project/input/"
Write-Output "\n文件上传成功!"
Write-Output "\n文件头部内容预览 (前10行):"
for ($i=1; $i -le 10; $i++) {
    Write-Output "这是测试文件的第 $i 行数据，用于HDFS练习"
}
Write-Output "\n文件尾部内容预览 (最后10行):"
for ($i=91; $i -le 100; $i++) {
    Write-Output "这是测试文件的第 $i 行数据，用于HDFS练习"
}
Write-Output "\n文件属性信息:"
Write-Output "drwxr-xr-x   - student supergroup          0 2024-01-01 00:00 /user/student/project/input/test.txt"
Write-Output "大小: 约 15KB | 副本数: 3 | 块大小: 128MB"

# 模拟文件操作和权限管理
Write-Output "\n[任务 1.3] 文件操作和权限管理"
Write-Output "-" * 60
Write-Output "执行命令: hdfs dfs -cp /user/student/project/input/test.txt /user/student/project/temp/"
Write-Output "执行命令: hdfs dfs -chmod 644 /user/student/project/temp/test.txt"
Write-Output "执行命令: hdfs dfs -chmod 755 /user/student/project/temp/"
Write-Output "\n权限设置成功!"
Write-Output "\n验证权限设置:"
Write-Output "drwxr-xr-x   - student supergroup          0 2024-01-01 00:00 /user/student/project/temp/"
Write-Output "-rw-r--r--   3 student supergroup      15000 2024-01-01 00:00 /user/student/project/temp/test.txt"

# 模拟批量文件操作
Write-Output "\n[任务 2.1] 批量文件上传"
Write-Output "-" * 60
Write-Output "执行命令: 创建5个本地测试文件 (file1.txt 到 file5.txt)"
Write-Output "执行命令: hdfs dfs -put file*.txt /user/student/project/input/"
Write-Output "\n批量上传成功!"
Write-Output "\n上传文件列表:"
for ($i=1; $i -le 5; $i++) {
    Write-Output "-rw-r--r--   3 student supergroup       1000 2024-01-01 00:00 /user/student/project/input/file$i.txt"
}

# 模拟通配符操作
Write-Output "\n[任务 2.2] 通配符操作"
Write-Output "-" * 60
Write-Output "执行命令: hdfs dfs -ls /user/student/project/input/*.txt"
Write-Output "输出结果:"
Write-Output "-rw-r--r--   3 student supergroup      15000 2024-01-01 00:00 /user/student/project/input/test.txt"
for ($i=1; $i -le 5; $i++) {
    Write-Output "-rw-r--r--   3 student supergroup       1000 2024-01-01 00:00 /user/student/project/input/file$i.txt"
}

Write-Output "\n执行命令: hdfs dfs -cp /user/student/project/input/file*.txt /user/student/project/temp/"
Write-Output "执行命令: hdfs dfs -ls /user/student/project/input/ | wc -l"
Write-Output "文件总数: 6"

# 模拟目录操作和清理
Write-Output "\n[任务 2.3] 目录操作和清理"
Write-Output "-" * 60
Write-Output "执行命令: hdfs dfs -mkdir -p /user/student/backup/"
Write-Output "执行命令: hdfs dfs -cp -r /user/student/project/ /user/student/backup/"
Write-Output "执行命令: hdfs dfs -rm /user/student/project/temp/*"
Write-Output "\n操作完成!"
Write-Output "\n备份目录结构:"
Write-Output "/user/student/backup/project/"
Write-Output "├── input/  (包含6个文件)"
Write-Output "├── output/"
Write-Output "└── temp/   (已清空)"

Write-Output "\n" * 2
Write-Output "=" * 60
Write-Output "模拟操作完成! 请在实际Hadoop环境中运行README.md中的命令"
Write-Output "=" * 60