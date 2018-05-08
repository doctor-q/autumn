package cc.doctor.framework.log.appender;

import cc.doctor.framework.log.event.Event;

import java.io.IOException;

public class ConsoleAppender extends OutputStreamAppender {

    @Override
    public void append(Event event) {
        byte[] bytes = encoder.encode(event);
        try {
            System.out.write(bytes);
            System.out.println();
        } catch (IOException e) {

        }
    }
}
