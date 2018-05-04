package cc.doctor.framework.log.appender;

import cc.doctor.framework.log.event.Event;

public class ConsoleAppender extends OutputStreamAppender {

    @Override
    public void append(Event event) {
        byte[] bytes = encoder.encode(event);
        write(bytes);
    }
}
