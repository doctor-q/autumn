package cc.doctor.framework.log.pattern.converter;

import cc.doctor.framework.log.event.Event;

public abstract class Converter {
    protected String format;
    protected String arg;

    public String getFormat() {
        return format;
    }

    public String getArg() {
        return arg;
    }

    public abstract String convert(Event event);

    public void setFormat(String format) {
        this.format = format;
    }

    public void setArg(String arg) {
        this.arg = arg;
    }
}
