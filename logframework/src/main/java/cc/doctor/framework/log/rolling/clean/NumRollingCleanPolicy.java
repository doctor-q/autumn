package cc.doctor.framework.log.rolling.clean;

import java.io.File;
import java.util.LinkedList;
import java.util.List;

/**
 * 基于文件数量的清除策略
 */
public class NumRollingCleanPolicy extends ThresholdRollingCleanPolicy {
    // 最大存在的文件数量
    private int maxFileNum = 1000;


    public int getMaxFileNum() {
        return maxFileNum;
    }

    public void setMaxFileNum(int maxFileNum) {
        this.maxFileNum = maxFileNum;
    }

    @Override
    public void clean(List<File> files) {
        List<File> fileList = new LinkedList<>(files);
        int size = fileList.size();
        if (size >= threshold * maxFileNum) {
            int cleanSize = (int) (cleanRate * size);
            List<File> deleteFiles = new LinkedList<>();
            for (int i = 0; i < cleanSize; i++) {
                deleteFiles.add(fileList.get(i));
            }
            for (File deleteFile : deleteFiles) {
                deleteFile.delete();
                files.remove(deleteFile);
            }
        }
    }
}
