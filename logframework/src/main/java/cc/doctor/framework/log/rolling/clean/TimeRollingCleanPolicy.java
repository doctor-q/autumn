package cc.doctor.framework.log.rolling.clean;

import java.io.File;
import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

/**
 * 基于时间的文件清除策略
 */
public class TimeRollingCleanPolicy implements RollingCleanPolicy {
    // 清除lastDays前的文件
    private int lastDays = 1;

    public int getLastDays() {
        return lastDays;
    }

    public void setLastDays(int lastDays) {
        this.lastDays = lastDays;
    }

    @Override
    public void clean(List<File> files) {
        Date nDaysAgo = getNDaysAgo(lastDays);
        Iterator<File> iterator = files.iterator();
        while (iterator.hasNext()) {
            File next = iterator.next();
            if (next.lastModified() < nDaysAgo.getTime()) {
                next.delete();
                iterator.remove();
            }
        }
    }

    private Date getNDaysAgo(int n) {
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DATE, 1 - n);
        calendar.set(Calendar.HOUR, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        return calendar.getTime();
    }
}
