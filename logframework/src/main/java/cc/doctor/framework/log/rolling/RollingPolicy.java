package cc.doctor.framework.log.rolling;

public abstract class RollingPolicy {
    private String filePattern;

    public String getFilePattern() {
        return filePattern;
    }

    public void setFilePattern(String filePattern) {
        this.filePattern = filePattern;
    }

    abstract public void rollover();
}
