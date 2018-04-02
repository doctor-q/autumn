package cc.doctor.framework.validate;

import java.lang.annotation.Annotation;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class ValidatorRegistry {
    private static Map<Annotation, Validator> validatorMap = new ConcurrentHashMap<>();

    public static Validator get(Annotation annotation) {
        return validatorMap.get(annotation);
    }
}
