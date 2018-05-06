package cc.doctor.framework.log.appender;

import cc.doctor.framework.log.appender.encode.Encoder;
import cc.doctor.framework.log.event.Event;

public interface Appender {
    String getName();

    void append(Event event);

    void setEncoder(Encoder encoder);
}
