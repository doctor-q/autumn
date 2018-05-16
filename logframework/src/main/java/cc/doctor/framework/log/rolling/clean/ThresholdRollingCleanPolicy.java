package cc.doctor.framework.log.rolling.clean;

public abstract class ThresholdRollingCleanPolicy implements RollingCleanPolicy {
    // 当当前值为最大数量的threshold倍，开始清理
    protected float threshold = 0.8f;
    // 清理文件，降低当前值的cleanRate倍
    protected float cleanRate = 0.25f;

    public float getThreshold() {
        return threshold;
    }

    public void setThreshold(float threshold) {
        this.threshold = threshold;
    }

    public float getCleanRate() {
        return cleanRate;
    }

    public void setCleanRate(float cleanRate) {
        this.cleanRate = cleanRate;
    }
}
