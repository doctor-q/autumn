package cc.doctor.framework.log.rolling;

public class SizeRollingPolicy extends RollingPolicy {
    // 一个文件的大小
    private Long size;

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
