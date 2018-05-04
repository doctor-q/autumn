package cc.doctor.framework.log.appender;

import cc.doctor.framework.log.event.Event;

public interface Appender {
    String getName();
    void appender(Event event);
}
