package cc.doctor.framework.log.rolling;

import java.util.Date;

public class TimeRollingPolicy extends RollingPolicy {
    // 间隔时间
    private Long duration;
    private Long lastLogFileTime;

    @Override
    public void rollover() {
        Date now = new Date();
        if (now.getTime() - lastLogFileTime > duration) {
            newFile();
        }
    }

    public Long getDuration() {
        return duration;
    }

    public void setDuration(Long duration) {
        this.duration = duration;
    }

    public Long getLastLogFileTime() {
        return lastLogFileTime;
    }

    public void setLastLogFileTime(Long lastLogFileTime) {
        this.lastLogFileTime = lastLogFileTime;
    }
}
