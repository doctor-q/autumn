package cc.doctor.framework.log.rolling;

public class TimeRollingPolicy extends RollingPolicy {
    // 间隔时间
    private Long duration;
    private Long lastLogFileTime;

    @Override
    public void rollover() {

    }
}
