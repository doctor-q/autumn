package cc.doctor.framework.log.pattern.converter;

import cc.doctor.framework.log.event.Event;

public class EchoConverter extends Converter {
    @Override
    public String convert(Event event) {
        return format;
    }
}