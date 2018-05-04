package cc.doctor.framework.log.event;

import java.util.Date;

public interface Event {
    Level getLevel();
    String getThread();
    String getMessage();
    Object[] getArgs();
    String getFormatMessage();
    Date getLogTime();
    String getLogger();
}
