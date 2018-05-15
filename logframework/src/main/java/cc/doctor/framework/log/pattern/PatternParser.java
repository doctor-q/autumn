package cc.doctor.framework.log.pattern;

import cc.doctor.framework.log.event.DefaultEvent;
import cc.doctor.framework.log.event.Level;
import cc.doctor.framework.log.pattern.converter.*;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;

public class PatternParser {
    private static PatternParser INSTANCE;
    private Map<String, Class<? extends Converter>> converterMap;
    public static final Map<String, Class<? extends Converter>> defaultConverterMap = new HashMap<>();

    static {
        defaultConverterMap.put("d", DateConverter.class);

        defaultConverterMap.put("le", LevelConverter.class);

        defaultConverterMap.put("t", ThreadConverter.class);

        defaultConverterMap.put("lo", LoggerConverter.class);

        defaultConverterMap.put("m", MessageConverter.class);

    }

    public Map<String, Class<? extends Converter>> getConverterMap() {
        return converterMap;
    }

    public void setConverterMap(Map<String, Class<? extends Converter>> converterMap) {
        this.converterMap = converterMap;
    }

    public static PatternParser getSingleton() {
        if (INSTANCE == null) {
            INSTANCE = new PatternParser();
            INSTANCE.setConverterMap(defaultConverterMap);
        }
        return INSTANCE;
    }

    /**
     * 将日志格式转换成converter列表
     *
     * @param pattern 日志格式，形如%d{yyyy-MM-dd HH:mm:ss.SSS} [%t] %-5le %lo{50} - %msg%n
     * @return converter列表
     */
    public List<Converter> parse(String pattern) {
        List<Converter> converters = new LinkedList<>();
        Converter currentConverter = null;
        // 普通字符串,EchoConverter
        StringBuilder stringBuilder = new StringBuilder();
        for (AtomicInteger i = new AtomicInteger(0); i.get() < pattern.length(); i.incrementAndGet()) {
            char c = pattern.charAt(i.get());
            if (c == '%') {
                if (stringBuilder.length() > 0) {
                    EchoConverter echoConverter = new EchoConverter();
                    echoConverter.setFormat(stringBuilder.toString());
                    stringBuilder = new StringBuilder();
                    converters.add(echoConverter);
                }
                String format = getFormat(i, pattern);
                Class<? extends Converter> converterClass = converterMap.get(format);
                try {
                    Constructor<? extends Converter> constructor = converterClass.getConstructor();
                    Converter converter = constructor.newInstance();
                    converter.setFormat(format);
                    converters.add(converter);
                    currentConverter = converter;
                } catch (NoSuchMethodException | IllegalAccessException | InstantiationException | InvocationTargetException e) {

                }
            } else if (c == '{') {
                String arg = getArg(i, pattern);
                if (currentConverter == null) {
                    throw new ConvertException();
                }
                currentConverter.setArg(arg);
            } else {
                stringBuilder.append(c);
            }
        }
        if (stringBuilder.length() > 0) {
            EchoConverter echoConverter = new EchoConverter();
            echoConverter.setFormat(stringBuilder.toString());
            converters.add(echoConverter);
        }
        return converters;
    }

    private String getArg(AtomicInteger i, String pattern) {
        i.getAndIncrement();
        StringBuilder stringBuilder = new StringBuilder();
        for (; i.get() < pattern.length(); i.getAndIncrement()) {
            char c = pattern.charAt(i.get());
            if (c == '}') {
                return stringBuilder.toString();
            } else {
                stringBuilder.append(c);
            }
        }
        throw new ConvertException();
    }

    private String getFormat(AtomicInteger i, String pattern) {
        i.getAndIncrement();
        StringBuilder stringBuilder = new StringBuilder();
        for (; i.get() < pattern.length(); i.getAndIncrement()) {
            char c = pattern.charAt(i.get());
            stringBuilder.append(c);
            if (converterMap.containsKey(stringBuilder.toString())) {
                return stringBuilder.toString();
            }
        }
        throw new ConvertException();
    }

    public static void main(String[] args) {
        List<Converter> converters = PatternParser.getSingleton().parse("%d{yyyy-MM-dd HH:mm:ss.SSS} [%t] %le %lo{50} - %m");
        DefaultEvent defaultEvent = new DefaultEvent().level(Level.INFO).logger("loooo")
                .logTime(new Date()).message("info[{}]").thread(Thread.currentThread().getName()).args("a");
        defaultEvent.prepareLog();
        for (Converter converter : converters) {
            System.out.print(converter.convert(defaultEvent));
        }
    }
}
