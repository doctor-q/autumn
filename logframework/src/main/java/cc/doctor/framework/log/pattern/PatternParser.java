package cc.doctor.framework.log.pattern;

import cc.doctor.framework.log.pattern.converter.*;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

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
     * @param pattern 日志格式，形如%d{yyyy-MM-dd HH:mm:ss.SSS} [%thread] %-5level %logger{50} - %msg%n
     * @return converter列表
     */
    public List<Converter> parse(String pattern) {
        List<Converter> converters = new LinkedList<>();
        Converter currentConverter = null;
        StringBuilder stringBuilder = new StringBuilder();
        for (int i = 0; i < pattern.length(); i++) {
            char c = pattern.charAt(i);
            if (c == '%') {
                if (stringBuilder.length() > 0) {
                    EchoConverter echoConverter = new EchoConverter();
                    converters.add(echoConverter);
                }
                String format = getFormat(i, pattern);
                Class<? extends Converter> converterClass = converterMap.get(format);
                try {
                    Constructor<? extends Converter> constructor = converterClass.getConstructor();
                    Converter converter = constructor.newInstance();
                    converter.setFormat(format);
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
        return converters;
    }

    private String getArg(int i, String pattern) {
        StringBuilder stringBuilder = new StringBuilder();
        for (; i < pattern.length(); i++) {
            char c = pattern.charAt(i);
            if (c == '}') {
                return stringBuilder.toString();
            } else {
                stringBuilder.append(c);
            }
        }
        throw new ConvertException();
    }

    private String getFormat(int i, String pattern) {
        StringBuilder stringBuilder = new StringBuilder();
        for (; i < pattern.length(); i++) {
            char c = pattern.charAt(i);
            stringBuilder.append(c);
            if (converterMap.containsKey(stringBuilder.toString())) {
                return stringBuilder.toString();
            }
        }
        throw new ConvertException();
    }
}
