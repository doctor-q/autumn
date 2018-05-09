package cc.doctor.framework.log.rolling.clean;


import java.io.File;
import java.util.*;

/**
 * 基于目录大小的文件清除策略
 */
public class SizeRollingCleanPolicy extends ThresoldRollingCleanPolicy {
    private long maxSize = 10 * 1024 * 1024 * 1024L;

    public long getMaxSize() {
        return maxSize;
    }

    public void setMaxSize(long maxSize) {
        this.maxSize = maxSize;
    }

    @Override
    public void clean(List<File> files) {
        // 按时间排序
        Map<File, Long> fileSize = new TreeMap<>(new Comparator<File>() {
            @Override
            public int compare(File o1, File o2) {
                return (int) (o1.lastModified() - o2.lastModified());
            }
        });
        long totalSize = 0L;
        for (File file : files) {
            long length = file.length();
            fileSize.put(file, length);
            totalSize += length;
        }
        if (totalSize >= threshold * maxSize) {
            long cleanTo = (long) (totalSize * (1 - cleanRate));
            List<File> deleteFiles = new LinkedList<>();
            for (Map.Entry<File, Long> entry : fileSize.entrySet()) {
                if (totalSize > cleanTo) {
                    deleteFiles.add(entry.getKey());
                    totalSize -= entry.getValue();
                }
            }
            for (File deleteFile : deleteFiles) {
                deleteFile.delete();
                files.remove(deleteFile);
            }
        }
    }
}
