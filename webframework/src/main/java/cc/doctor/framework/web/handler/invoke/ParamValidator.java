package cc.doctor.framework.web.handler.invoke;


import java.lang.annotation.Annotation;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public abstract class ParamValidator {
    private static Map<Class<? extends Annotation>, ParamValidator> paramValidatorMap = new ConcurrentHashMap<>();

    abstract void validate(String field, Object value) throws InvalidParamException;

    public static boolean contains(Class<? extends Annotation> annoClazz) {
        if (annoClazz == null) {
            return false;
        }
        return paramValidatorMap.containsKey(annoClazz);
    }

    public static ParamValidator get(Class<? extends Annotation> annoClazz) {
        if (annoClazz == null) {
            return null;
        }
        return paramValidatorMap.get(annoClazz);
    }
}
