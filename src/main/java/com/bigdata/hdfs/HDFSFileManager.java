package com.bigdata.hdfs;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.*;
import org.apache.hadoop.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.net.URI;

/**
 * HDFS 文件管理器
 * 提供基本的 HDFS 文件操作功能
 */
public class HDFSFileManager {
    private static final Logger logger = LoggerFactory.getLogger(HDFSFileManager.class);

    private FileSystem fileSystem;
    private Configuration configuration;

    /**
     * 构造函数，初始化 HDFS 连接
     * @param hdfsUri HDFS 的 URI，例如 "hdfs://localhost:9000"
     */
    public HDFSFileManager(String hdfsUri) throws IOException {
        try {
            // 1. 创建 Configuration 对象
            configuration = new Configuration();
            // 2. 设置 HDFS URI
            configuration.set("fs.defaultFS", hdfsUri);
            // 3. 获取 FileSystem 实例
            fileSystem = FileSystem.get(URI.create(hdfsUri), configuration);
            logger.info("成功连接到 HDFS: {}", hdfsUri);
        } catch (IOException e) {
            logger.error("连接 HDFS 失败: {}", hdfsUri, e);
            throw e;
        }
    }

    /**
     * 上传本地文件到 HDFS
     * @param localPath 本地文件路径
     * @param hdfsPath HDFS 目标路径
     * @param overwrite 是否覆盖已存在的文件
     * @return 上传是否成功
     */
    public boolean uploadFile(String localPath, String hdfsPath, boolean overwrite) {
        try {
            // 1. 检查本地文件是否存在
            File localFile = new File(localPath);
            if (!localFile.exists() || !localFile.isFile()) {
                logger.error("本地文件不存在或不是文件: {}", localPath);
                return false;
            }

            // 2. 创建 HDFS 目标目录（如果不存在）
            Path hdfsFilePath = new Path(hdfsPath);
            Path parentDir = hdfsFilePath.getParent();
            if (parentDir != null && !fileSystem.exists(parentDir)) {
                fileSystem.mkdirs(parentDir);
                logger.info("创建 HDFS 目录: {}", parentDir);
            }

            // 3. 执行文件上传
            fileSystem.copyFromLocalFile(!overwrite, new Path(localPath), hdfsFilePath);
            logger.info("文件上传成功: 本地={} -> HDFS={}", localPath, hdfsPath);
            return true;
        } catch (IOException e) {
            logger.error("文件上传失败: 本地={} -> HDFS={}", localPath, hdfsPath, e);
            return false;
        }
    }

    /**
     * 从 HDFS 下载文件到本地
     * @param hdfsPath HDFS 文件路径
     * @param localPath 本地目标路径
     * @param overwrite 是否覆盖已存在的文件
     * @return 下载是否成功
     */
    public boolean downloadFile(String hdfsPath, String localPath, boolean overwrite) {
        try {
            // 检查 HDFS 文件是否存在
            Path hdfsFilePath = new Path(hdfsPath);
            if (!fileSystem.exists(hdfsFilePath) || fileSystem.isDirectory(hdfsFilePath)) {
                logger.error("HDFS 文件不存在或不是文件: {}", hdfsPath);
                return false;
            }

            // 检查本地文件是否存在
            File localFile = new File(localPath);
            if (localFile.exists() && !overwrite) {
                logger.error("本地文件已存在且不允许覆盖: {}", localPath);
                return false;
            }

            // 创建本地父目录
            File parentDir = localFile.getParentFile();
            if (parentDir != null && !parentDir.exists()) {
                parentDir.mkdirs();
            }

            // 执行文件下载
            fileSystem.copyToLocalFile(false, hdfsFilePath, new Path(localPath));
            logger.info("文件下载成功: HDFS={} -> 本地={}", hdfsPath, localPath);
            return true;
        } catch (IOException e) {
            logger.error("文件下载失败: HDFS={} -> 本地={}", hdfsPath, localPath, e);
            return false;
        }
    }

    /**
     * 删除 HDFS 中的文件或目录
     * @param hdfsPath HDFS 路径
     * @param recursive 是否递归删除（用于目录）
     * @return 删除是否成功
     */
    public boolean deleteFile(String hdfsPath, boolean recursive) {
        try {
            Path path = new Path(hdfsPath);
            if (!fileSystem.exists(path)) {
                logger.error("HDFS 路径不存在: {}", hdfsPath);
                return false;
            }

            if (fileSystem.isDirectory(path) && !recursive) {
                logger.error("无法删除非空目录，请设置 recursive=true: {}", hdfsPath);
                return false;
            }

            boolean success = fileSystem.delete(path, recursive);
            if (success) {
                logger.info("删除成功: {}", hdfsPath);
            } else {
                logger.error("删除失败: {}", hdfsPath);
            }
            return success;
        } catch (IOException e) {
            logger.error("删除操作失败: {}", hdfsPath, e);
            return false;
        }
    }

    /**
     * 递归列出目录中的所有文件和子目录
     * @param hdfsPath HDFS 目录路径
     * @param depth 当前递归深度（用于格式化输出）
     */
    public void listDirectory(String hdfsPath, int depth) {
        try {
            Path path = new Path(hdfsPath);
            if (!fileSystem.exists(path)) {
                logger.error("路径不存在: {}", hdfsPath);
                return;
            }

            // 生成缩进
            StringBuilder indent = new StringBuilder();
            for (int i = 0; i < depth; i++) {
                indent.append("  ");
            }

            if (fileSystem.isDirectory(path)) {
                // 打印目录信息
                System.out.println(indent + "+ " + path.getName() + "/");
                
                // 获取目录内容
                RemoteIterator<LocatedFileStatus> iterator = fileSystem.listFiles(path, false);
                while (iterator.hasNext()) {
                    LocatedFileStatus fileStatus = iterator.next();
                    String fileName = fileStatus.getPath().getName();
                    long fileSize = fileStatus.getLen();
                    System.out.println(indent + "  - " + fileName + " (" + formatSize(fileSize) + ")");
                }

                // 列出子目录并递归处理
                FileStatus[] statuses = fileSystem.listStatus(path);
                for (FileStatus status : statuses) {
                    if (status.isDirectory() && !status.getPath().getName().equals(".") && !status.getPath().getName().equals("..")) {
                        listDirectory(status.getPath().toString(), depth + 1);
                    }
                }
            } else {
                // 如果是文件，直接打印信息
                FileStatus status = fileSystem.getFileStatus(path);
                System.out.println(indent + "- " + path.getName() + " (" + formatSize(status.getLen()) + ")");
            }
        } catch (IOException e) {
            logger.error("列出目录内容失败: {}", hdfsPath, e);
        }
    }

    /**
     * 统计目录信息
     * @param hdfsPath HDFS 目录路径
     * @return DirectoryStats 对象，包含统计信息
     */
    public DirectoryStats getDirectoryStats(String hdfsPath) {
        DirectoryStats stats = new DirectoryStats();
        try {
            Path path = new Path(hdfsPath);
            if (!fileSystem.exists(path)) {
                logger.error("路径不存在: {}", hdfsPath);
                return stats;
            }

            collectDirectoryStats(path, stats);
        } catch (IOException e) {
            logger.error("统计目录信息失败: {}", hdfsPath, e);
        }
        return stats;
    }

    /**
     * 递归收集目录统计信息
     */
    private void collectDirectoryStats(Path path, DirectoryStats stats) throws IOException {
        FileStatus status = fileSystem.getFileStatus(path);
        
        if (status.isDirectory()) {
            stats.directoryCount++;
            
            FileStatus[] statuses = fileSystem.listStatus(path);
            for (FileStatus childStatus : statuses) {
                collectDirectoryStats(childStatus.getPath(), stats);
            }
        } else {
            stats.fileCount++;
            stats.totalSize += status.getLen();
        }
    }

    /**
     * 关闭 HDFS 连接
     */
    public void close() {
        if (fileSystem != null) {
            try {
                fileSystem.close();
                logger.info("HDFS 连接已关闭");
            } catch (IOException e) {
                logger.error("关闭 HDFS 连接失败", e);
            }
        }
    }

    /**
     * 目录统计信息类
     */
    public static class DirectoryStats {
        private long fileCount;
        private long directoryCount;
        private long totalSize;

        // 构造函数
        public DirectoryStats() {
            this.fileCount = 0;
            this.directoryCount = 0;
            this.totalSize = 0;
        }

        // getter 方法
        public long getFileCount() {
            return fileCount;
        }

        public long getDirectoryCount() {
            return directoryCount;
        }

        public long getTotalSize() {
            return totalSize;
        }

        @Override
        public String toString() {
            return "DirectoryStats{" +
                    "文件数量=" + fileCount +
                    ", 目录数量=" + directoryCount +
                    ", 总大小=" + formatSize(totalSize) +
                    '}';
        }
    }

    /**
     * 格式化文件大小显示
     */
    private static String formatSize(long bytes) {
        if (bytes < 1024) return bytes + " B";
        else if (bytes < 1048576) return (bytes / 1024) + " KB";
        else if (bytes < 1073741824) return (bytes / 1048576) + " MB";
        else return (bytes / 1073741824) + " GB";
    }

    /**
     * 主方法，用于测试 HDFS 文件管理器
     */
    public static void main(String[] args) {
        HDFSFileManager manager = null;
        try {
            // 1. 创建 HDFSFileManager 实例
            String hdfsUri = "hdfs://localhost:9000";
            manager = new HDFSFileManager(hdfsUri);

            // 准备测试目录
            String testDir = "/user/student/test_program";
            manager.deleteFile(testDir, true); // 先删除旧的测试目录
            manager.fileSystem.mkdirs(new Path(testDir));

            // 2. 测试文件上传功能
            System.out.println("\n=== 测试文件上传 ===");
            // 创建本地测试文件
            String localTestFile = "test_upload.txt";
            try (FileWriter writer = new FileWriter(localTestFile)) {
                for (int i = 1; i <= 20; i++) {
                    writer.write("这是测试文件的第 " + i + " 行数据\n");
                }
            }
            
            boolean uploadResult = manager.uploadFile(localTestFile, testDir + "/uploaded_file.txt", true);
            System.out.println("文件上传" + (uploadResult ? "成功" : "失败"));

            // 3. 测试目录遍历功能
            System.out.println("\n=== 测试目录遍历 ===");
            System.out.println("目录结构:");
            manager.listDirectory(testDir, 0);

            // 4. 测试文件下载功能
            System.out.println("\n=== 测试文件下载 ===");
            String downloadPath = "downloaded_file.txt";
            boolean downloadResult = manager.downloadFile(testDir + "/uploaded_file.txt", downloadPath, true);
            System.out.println("文件下载" + (downloadResult ? "成功" : "失败"));

            // 5. 测试目录统计功能
            System.out.println("\n=== 测试目录统计 ===");
            DirectoryStats stats = manager.getDirectoryStats(testDir);
            System.out.println(stats);

            // 6. 测试文件删除功能
            System.out.println("\n=== 测试文件删除 ===");
            boolean deleteResult = manager.deleteFile(testDir + "/uploaded_file.txt", false);
            System.out.println("文件删除" + (deleteResult ? "成功" : "失败"));

            // 清理本地测试文件
            new File(localTestFile).delete();
            new File(downloadPath).delete();

        } catch (Exception e) {
            logger.error("程序执行出错", e);
            e.printStackTrace();
        } finally {
            if (manager != null) {
                manager.close();
            }
        }
    }
}