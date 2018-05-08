package cc.doctor.framework.log.pattern.converter;

import cc.doctor.framework.log.event.Event;

import java.text.SimpleDateFormat;

public class DateConverter extends Converter {
    private String pattern;
    private SimpleDateFormat simpleDateFormat;

    @Override
    public String convert(Event event) {
        if (simpleDateFormat == null) {
            pattern = getArg();
            simpleDateFormat = new SimpleDateFormat(pattern);
        }
        return simpleDateFormat.format(event.getLogTime());
    }

    public String getPattern() {
        return pattern;
    }
}
