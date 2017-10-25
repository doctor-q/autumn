package cc.doctor.framework.web.handler.in;

import cc.doctor.framework.utils.DateUtils;

import java.util.Arrays;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by doctor on 17-6-9.
 */
public class RequestHandlerFactory {
    public static Map<Class, RequestAnnotationHandler> annotationHandlerMap;

    static {
        annotationHandlerMap = new ConcurrentHashMap<>();
        registerRequestAnnotationHandler(DateParse.class, new RequestAnnotationHandler<DateParse>() {

            @Override
            public Object handler(String parameter, DateParse dateParse) {
                return DateUtils.parse(parameter, dateParse.pattern());
            }
        });
        registerRequestAnnotationHandler(DataMapper.class, new RequestAnnotationHandler<DataMapper>() {
            @Override
            public Object handler(String parameter, DataMapper dataMapper) {
                if (dataMapper.froms().length != dataMapper.tos().length) {
                    return parameter;
                }
                if (dataMapper.arrow().equals("<->") || dataMapper.arrow().equals("->")) {
                    for (int i = 0; i < dataMapper.froms().length; i++) {
                        if (parameter.equals(dataMapper.froms()[i])) {
                            return dataMapper.tos()[i];
                        }
                    }
                }
                return parameter;
            }
        });
        registerRequestAnnotationHandler(EnumDataMapper.class, new RequestAnnotationHandler<EnumDataMapper>() {
            @Override
            public Object handler(String parameter, EnumDataMapper annotation) {
                Map fromToMap = EnumDataMapperHolder.enumDataMapperHolder.fromToMap(annotation);
                if (fromToMap != null) {
                    return fromToMap.get(parameter);
                }
                return null;
            }
        });
        registerRequestAnnotationHandler(ListSplit.class, new RequestAnnotationHandler<ListSplit>() {
            @Override
            public Object handler(String parameter, ListSplit annotation) {
                if (parameter != null) {
                    return Arrays.asList(parameter.split(annotation.separator()));
                }
                return null;
            }
        });
    }

    public static void registerRequestAnnotationHandler(Class annotationClass, RequestAnnotationHandler requestAnnotationHandler) {
        annotationHandlerMap.put(annotationClass, requestAnnotationHandler);
    }

    public static RequestAnnotationHandler getAnnotationHandler(Class annotation) {
        return annotationHandlerMap.get(annotation);
    }
}
