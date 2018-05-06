package cc.doctor.framework.log.appender.encode;

import cc.doctor.framework.log.pattern.converter.Converter;
import cc.doctor.framework.log.pattern.PatternParser;
import cc.doctor.framework.log.event.Event;

import java.util.List;

public class PatternEncoder implements Encoder {

    private String pattern;

    public String getPattern() {
        return pattern;
    }

    public void setPattern(String pattern) {
        this.pattern = pattern;
    }

    private String format(Event event) {
        PatternParser singleton = PatternParser.getSingleton();
        List<Converter> converters = singleton.parse(pattern);
        StringBuilder stringBuilder = new StringBuilder();
        for (Converter converter : converters) {
            String convert = converter.convert(event);
            stringBuilder.append(convert);
        }
        return stringBuilder.toString();
    }

    @Override
    public byte[] encode(Event event) {
        String format = format(event);
        return format.getBytes();
    }
}
