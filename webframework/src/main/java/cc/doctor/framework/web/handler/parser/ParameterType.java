package cc.doctor.framework.web.handler.parser;

import cc.doctor.framework.utils.ReflectUtils;
import cc.doctor.framework.utils.SerializeUtils;
import cc.doctor.framework.web.handler.in.RequestAnnotationHandler;
import cc.doctor.framework.web.handler.in.RequestHandlerFactory;
import cc.doctor.framework.web.servlet.meta.HttpMetaData;
import cc.doctor.framework.web.servlet.meta.HttpMetaParameters;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpServletRequest;
import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Created by doctor on 2017/7/21.
 */
public enum ParameterType {
    HTTP_META_DATA(HttpMetaData.class, null) {
        @Override
        public Object transfer(Class<?> paramType, Annotation[] paramAnnotations, HttpMetaData httpMetaData, HttpMetaParameters httpMetaParameters) {
            return httpMetaData;
        }
    },
    HTTP_META_PARAMETERS(HttpMetaParameters.class, null) {
        @Override
        public Object transfer(Class<?> paramType, Annotation[] paramAnnotations, HttpMetaData httpMetaData, HttpMetaParameters httpMetaParameters) {
            return httpMetaParameters;
        }
    },
    UNPACK(Unpack.class, null) {
        @Override
        public Object transfer(Class<?> paramType, Annotation[] paramAnnotations, HttpMetaData httpMetaData, HttpMetaParameters httpMetaParameters) {
            try {
                // unpack can do a before after hook
                Object instance = paramType.newInstance();
                ((Unpack)instance).beforeUnpack(httpMetaData, httpMetaParameters);
                fillObject(httpMetaParameters, instance);
                ((Unpack)instance).afterUnpack(httpMetaData, httpMetaParameters);
                return instance;
            } catch (InstantiationException | IllegalAccessException e) {
                log.error("", e);
            }
            return null;
        }
    },
    JSON(null, JsonParam.class) {
        @Override
        public Object transfer(Class<?> paramType, Annotation[] paramAnnotations, HttpMetaData httpMetaData, HttpMetaParameters httpMetaParameters) {
            return SerializeUtils.jsonToObject(httpMetaParameters.getJson(), paramType);
        }
    },
    FORM(null, Form.class) {
        @Override
        public Object transfer(Class<?> paramType, Annotation[] paramAnnotations, HttpMetaData httpMetaData, HttpMetaParameters httpMetaParameters) {
            Form form = (Form) getAnnotation(paramAnnotations, Form.class);
            if (form != null) {
                try {
                    Object instance = paramType.newInstance();
                    fillObject(httpMetaParameters, instance);
                    return instance;
                } catch (InstantiationException | IllegalAccessException e) {
                    log.error("", e);
                }
            }
            return null;
        }
    },
    PARAM(null, Param.class) {
        @Override
        public Object transfer(Class<?> paramType, Annotation[] paramAnnotations, HttpMetaData httpMetaData, HttpMetaParameters httpMetaParameters) {
            Param param = (Param) getAnnotation(paramAnnotations, Param.class);
            String name = param.value();
            String value = httpMetaParameters.getParamString(name);
            if (value == null) {
                return null;
            }
            return parseParam(value, paramType);
        }
    };
    private static final Logger log = LoggerFactory.getLogger(ReflectUtils.class);
    Class paramType;
    Class<? extends Annotation> paramAnnotation;

    ParameterType(Class paramType, Class<? extends Annotation> paramAnnotation) {
        this.paramType = paramType;
        this.paramAnnotation = paramAnnotation;
    }

    public static ParameterType get(Class<?> paramType, Annotation[] paramAnnotations) {
        for (ParameterType parameterType : ParameterType.values()) {
            if (paramType != null &&
                    parameterType.paramType != null &&
                    paramType.isAssignableFrom(parameterType.paramType)) {
                return parameterType;
            }
            if (paramAnnotations != null && paramAnnotations.length > 0) {
                for (Annotation paramAnnotation : paramAnnotations) {
                    if (paramAnnotation.annotationType().equals(parameterType.paramAnnotation)) {
                        return parameterType;
                    }
                }
            }
        }
        return null;
    }

    public Object transfer(Class<?> paramType, Annotation[] paramAnnotations, HttpMetaData httpMetaData, HttpMetaParameters httpMetaParameters) {
        return null;
    }

    public Annotation getAnnotation(Annotation[] annotations, Class annotationClass) {
        for (Annotation annotation : annotations) {
            if (annotation.annotationType().equals(annotationClass)) {
                return annotation;
            }
        }
        return null;
    }

    public void fillObject(HttpMetaParameters httpMetaParameters, Object object) {
        Map<String, Field> objectAttrNameFields = ReflectUtils.getObjectAttrNameFields(object.getClass());
        for (String param : objectAttrNameFields.keySet()) {
            Field field = objectAttrNameFields.get(param);
            String parameter = httpMetaParameters.getParamString(param);
            //do annotation
            if (field.isAnnotationPresent(IgnoreNull.class) && parameter == null) { //ignore null
                continue;
            }
            if (field.isAnnotationPresent(IgnoreEmpty.class)) {     //ignore empty
                IgnoreEmpty ignoreEmpty = field.getAnnotation(IgnoreEmpty.class);
                if (ignoreEmpty.trim()) {
                    if (parameter.trim().isEmpty()) {
                        continue;
                    }
                } else {
                    if (parameter.isEmpty()) {
                        continue;
                    }
                }
            }
            Annotation[] annotations = field.getAnnotations();
            for (Annotation annotation : annotations) {
                RequestAnnotationHandler requestAnnotationHandler = RequestHandlerFactory.getAnnotationHandler(annotation.getClass());
                requestAnnotationHandler.handler(parameter, annotation);
            }
            Object value = parseParam(parameter, field.getType());
            ReflectUtils.set(param, value, object);
        }
    }

    public Object parseParam(String parameter, Class type) {
        if (parameter == null || type == null) {
            return null;
        }
        Object value = null;
        if (type.equals(Integer.class)) {
            value = Integer.parseInt(parameter);
        } else if (type.equals(Long.class)) {
            value = Long.parseLong(parameter);
        } else if (type.equals(Float.class)) {
            value = Float.parseFloat(parameter);
        } else if (type.equals(Double.class)) {
            value = Double.parseDouble(parameter);
        } else if (type.equals(BigDecimal.class)) {
            value = BigDecimal.valueOf(Double.parseDouble(parameter));
        } else if (type.equals(List.class)) {
            value = Arrays.asList(parameter.split(","));
        } else if (type.equals(Set.class)) {
            try {
                Set<String> set = (Set<String>) type.newInstance();
                set.addAll(Arrays.asList(parameter.split(",")));
                value = set;
            } catch (InstantiationException | IllegalAccessException e) {
                log.error("", e);
            }

        } else if (type.equals(String.class)) {
            value = parameter;
        }
        return value;
    }
}
