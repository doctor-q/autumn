package cc.doctor.framework.jdbc.dao;

import cc.doctor.framework.jdbc.annotation.SqlFunction;
import cc.doctor.framework.jdbc.annotation.transform.DateFormat;
import cc.doctor.framework.jdbc.annotation.transform.Decrypt;
import cc.doctor.framework.jdbc.annotation.transform.Encrypt;
import cc.doctor.framework.jdbc.sql.SqlFunctions;
import cc.doctor.framework.utils.DateUtils;
import cc.doctor.framework.utils.EncryptUtils;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.util.Date;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by doctor on 2017/8/8.
 */
public abstract class AnnotationHandler<A extends Annotation> {
    private static Map<Class, AnnotationHandler> annotationHandlers = new ConcurrentHashMap<>();

    abstract public Object handler(A annotation, Object origin);

    public static <A extends Annotation> void registerAnnotationHandler(Class<A> annotationClass, AnnotationHandler<A> annotationHandler) {
        annotationHandlers.put(annotationClass, annotationHandler);
    }

    static {
        registerAnnotationHandler(Encrypt.class, new AnnotationHandler<Encrypt>() {
            @Override
            public Object handler(Encrypt annotation, Object origin) {
                if (origin == null) {
                    return null;
                }
                try {
                    return EncryptUtils.encrypt(origin.toString(), annotation.secretKey());
                } catch (Exception e) {
                    return null;
                }
            }
        });

        registerAnnotationHandler(Decrypt.class, new AnnotationHandler<Decrypt>() {
            @Override
            public Object handler(Decrypt annotation, Object origin) {
                if (origin == null) {
                    return null;
                }
                try {
                    return EncryptUtils.decrypt(origin.toString(), annotation.secretKey());
                } catch (Exception e) {
                    return null;
                }
            }
        });

        registerAnnotationHandler(DateFormat.class, new AnnotationHandler<DateFormat>() {
            @Override
            public Object handler(DateFormat annotation, Object origin) {
                if (origin instanceof Date) {
                    DateUtils.format((Date) origin, annotation.pattern());
                }
                return null;
            }
        });
    }

    public static Object handlerAll(Object data, Field field) {
        Annotation[] annotations = field.getAnnotations();
        for (Annotation annotation : annotations) {
            AnnotationHandler annotationHandler = annotationHandlers.get(annotation.annotationType());
            if (annotationHandler != null) {
                data = annotationHandler.handler(annotation, data);
            }
        }
        return data;
    }
}
