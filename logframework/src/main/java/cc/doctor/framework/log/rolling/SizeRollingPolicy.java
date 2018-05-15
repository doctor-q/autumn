package cc.doctor.framework.log.rolling;

public class SizeRollingPolicy extends RollingPolicy {
    private static final long DEFAULT_FILE_SIZE = 50 * 1024 * 1024L;
    // 一个文件的大小
    private Long size = DEFAULT_FILE_SIZE;

    @Override
    public void rollover() {
        if (currentFile.length() >= size) {
            newFile();
        }
    }

    public Long getSize() {
        return size;
    }

    public void setSize(Long size) {
        this.size = size;
    }
}
