package cc.doctor.framework.log.appender.encode;

import cc.doctor.framework.log.event.Event;

public interface Encoder {
    byte[] encode(Event event);
}
