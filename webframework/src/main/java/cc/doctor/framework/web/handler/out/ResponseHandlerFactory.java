package cc.doctor.framework.web.handler.out;

import cc.doctor.framework.utils.CollectionUtils;
import cc.doctor.framework.utils.DateUtils;

import java.util.*;

/**
 * Created by doctor on 2017/6/29.
 */
public class ResponseHandlerFactory {
    public static Map<Class, ResponseAnnotationHandler> annotationHandlerMap;

    static {
        annotationHandlerMap = new LinkedHashMap<>();
        registerResponseAnnotationHandler(DateFormat.class, new ResponseAnnotationHandler<DateFormat>() {
            @Override
            public Object handler(Object origin, DateFormat dateFormat) {
                if (!(origin instanceof Date)) {
                    return origin;
                }
                return DateUtils.format((Date) origin, dateFormat.pattern());
            }
        });
        registerResponseAnnotationHandler(ListJoin.class, new ResponseAnnotationHandler<ListJoin>() {
            @Override
            public Object handler(Object origin, ListJoin annotation) {
                if (origin instanceof Collection) {
                    return CollectionUtils.join((Collection)origin, annotation.on());
                }
                return origin;
            }
        });
    }

    public static void registerResponseAnnotationHandler(Class annotationClass, ResponseAnnotationHandler responseAnnotationHandler) {
        annotationHandlerMap.put(annotationClass, responseAnnotationHandler);
    }

    public static ResponseAnnotationHandler getAnnotationHandler(Class annotation) {
        return annotationHandlerMap.get(annotation);
    }
}
