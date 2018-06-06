package cc.doctor.framework.web.aop;

import java.lang.annotation.Annotation;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by doctor on 2018/1/21.
 */
public class AopRegistry {

    private static Map<Class<? extends Annotation>, AopHandler> aopHandlerMap = new ConcurrentHashMap<>();

    public static void register(Class<? extends Annotation> annotationType, AopHandler aopHandler) {
        aopHandlerMap.put(annotationType, aopHandler);
    }

    public static boolean contains(Class<? extends Annotation> annotationType) {
        if (annotationType == null) {
            return false;
        }
        return aopHandlerMap.containsKey(annotationType);
    }

    public static <A extends Annotation> AopHandler getAopHandler(A annotation) {
        if (annotation == null) {
            return null;
        }
        return aopHandlerMap.get(annotation.annotationType());
    }

    public static Map<Class<? extends Annotation>, AopHandler> getAopHandlerMap() {
        return aopHandlerMap;
    }
}
