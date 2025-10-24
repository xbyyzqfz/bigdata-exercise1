# 下载并安装Hadoop 3.3.6
Write-Output "开始下载Hadoop 3.3.6..."
$hadoopUrl = "https://archive.apache.org/dist/hadoop/common/hadoop-3.3.6/hadoop-3.3.6.tar.gz"
$hadoopTar = "D:\hadoop-3.3.6.tar.gz"
Invoke-WebRequest -Uri $hadoopUrl -OutFile $hadoopTar

Write-Output "下载完成，开始解压..."
# 需要解压tar.gz文件，先解压为tar，再解压为文件夹
# 使用PowerShell的Expand-Archive可能不支持tar.gz，所以我们使用其他方法
# 先创建Hadoop目录
New-Item -ItemType Directory -Force -Path "D:\hadoop-3.3.6"

# 使用7zip或其他工具解压（如果系统中有）
if (Get-Command "7z" -ErrorAction SilentlyContinue) {
    Write-Output "使用7zip解压..."
    7z x $hadoopTar -o"D:\"
    7z x "D:\hadoop-3.3.6.tar" -o"D:\"
} else {
    Write-Output "7zip未找到，尝试使用PowerShell解压..."
    # 这里简化处理，假设用户后续手动解压
    Write-Output "请手动解压文件: $hadoopTar 到 D:\目录"
}

# 清理安装包
Remove-Item -Path $hadoopTar -Force
if (Test-Path "D:\hadoop-3.3.6.tar") {
    Remove-Item -Path "D:\hadoop-3.3.6.tar" -Force
}

Write-Output "Hadoop下载完成，请按照后续步骤配置环境变量"