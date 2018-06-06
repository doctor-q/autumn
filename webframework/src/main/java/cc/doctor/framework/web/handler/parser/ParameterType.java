package cc.doctor.framework.web.handler.parser;

import cc.doctor.framework.utils.Container;
import cc.doctor.framework.utils.ReflectUtils;
import cc.doctor.framework.utils.SerializeUtils;
import cc.doctor.framework.web.handler.in.RequestAnnotationHandler;
import cc.doctor.framework.web.handler.in.RequestHandlerFactory;
import cc.doctor.framework.web.handler.invoke.Parameter;
import cc.doctor.framework.web.servlet.meta.HttpMetadata;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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
    HTTP_META_PARAMETERS(HttpMetadata.class, null) {
        @Override
        public void setParameter(Parameter parameter, HttpMetadata httpMetadata) {
            parameter.setValue(httpMetadata);
        }
    },
    UNPACK(Unpack.class, null) {
        @Override
        public void setParameter(Parameter parameter, HttpMetadata httpMetadata) {
            try {
                // unpack can do a before after hook
                Object instance = paramType.newInstance();
                parameter.setValue(instance);
                ((Unpack) instance).beforeUnpack(httpMetadata);
                fillObject(httpMetadata, parameter);
                ((Unpack) instance).afterUnpack(httpMetadata);
            } catch (InstantiationException | IllegalAccessException e) {
                log.error("", e);
            }
        }
    },
    JSON(null, JsonParam.class) {
        @Override
        public void setParameter(Parameter parameter, HttpMetadata httpMetadata) {
            Object object = SerializeUtils.jsonToObject(httpMetadata.getJson(), paramType);
            parameter.setValue(object);
        }
    },
    FORM(null, Form.class) {
        @Override
        public void setParameter(Parameter parameter, HttpMetadata httpMetadata) {
            Form form = (Form) getAnnotation(parameter.getAnnotations(), Form.class);
            if (form != null) {
                fillObject(httpMetadata, parameter);
            }
        }
    },
    PARAM(null, Param.class) {
        @Override
        public void setParameter(Parameter parameter, HttpMetadata httpMetadata) {
            Param param = (Param) getAnnotation(parameter.getAnnotations(), Param.class);
            String name = param.value();
            String value = httpMetadata.getParam(name);
            if (value == null) {
                return;
            }
            parameter.setValue(parseParam(value, parameter.getType()));
        }
    },
    ATTRIBUTE(null, Attribute.class) {
        @Override
        public void setParameter(Parameter parameter, HttpMetadata httpMetadata) {
            Attribute param = (Attribute) getAnnotation(parameter.getAnnotations(), Attribute.class);
            String name = param.value();
            String value = httpMetadata.getAttribute(name);
            if (value == null) {
                return;
            }
            parameter.setValue(parseParam(value, parameter.getType()));
        }
    },
    COOKIE(null, Cookie.class) {
        @Override
        public void setParameter(Parameter parameter, HttpMetadata httpMetadata) {
            Cookie param = (Cookie) getAnnotation(parameter.getAnnotations(), Cookie.class);
            String name = param.value();
            String value = httpMetadata.getCookie(name);
            if (value == null) {
                return;
            }
            parameter.setValue(parseParam(value, parameter.getType()));
        }
    }, HEADER(null, Header.class) {
        @Override
        public void setParameter(Parameter parameter, HttpMetadata httpMetadata) {
            Header param = (Header) getAnnotation(parameter.getAnnotations(), Header.class);
            String name = param.value();
            String value = httpMetadata.getHeader(name);
            if (value == null) {
                return;
            }
            parameter.setValue(parseParam(value, parameter.getType()));
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


    public void setParameter(Parameter parameter, HttpMetadata httpMetadata) {
    }

    public Annotation getAnnotation(Annotation[] annotations, Class annotationClass) {
        for (Annotation annotation : annotations) {
            if (annotation.annotationType().equals(annotationClass)) {
                return annotation;
            }
        }
        return null;
    }

    public void fillObject(HttpMetadata httpMetaParameters, Parameter parameter) {
        if (parameter.getValue() != null) {
            parameter.setValue(Container.container.newComponent(parameter.getType()));
        }
        Map<String, Field> objectAttrNameFields = ReflectUtils.getObjectAttrNameFields(parameter.getType());
        for (String name : objectAttrNameFields.keySet()) {
            Field field = objectAttrNameFields.get(name);
            String param = null;
            if (field.isAnnotationPresent(Attribute.class)) {
                param = httpMetaParameters.getAttribute(name);
            } else if (field.isAnnotationPresent(Cookie.class)) {
                param = httpMetaParameters.getCookie(name);
            } else {
                param = httpMetaParameters.getParam(name);
            }
            //do annotation
            if (field.isAnnotationPresent(IgnoreNull.class) && param == null) { //ignore null
                continue;
            }
            if (field.isAnnotationPresent(IgnoreEmpty.class)) {     //ignore empty
                IgnoreEmpty ignoreEmpty = field.getAnnotation(IgnoreEmpty.class);
                if (ignoreEmpty.trim()) {
                    if (param.trim().isEmpty()) {
                        continue;
                    }
                } else {
                    if (param.isEmpty()) {
                        continue;
                    }
                }
            }
            Annotation[] annotations = field.getAnnotations();
            for (Annotation annotation : annotations) {
                RequestAnnotationHandler requestAnnotationHandler = RequestHandlerFactory.getAnnotationHandler(annotation.getClass());
                requestAnnotationHandler.handler(param, annotation);
            }
            Object value = parseParam(param, field.getType());
            ReflectUtils.set(param, value, parameter.getValue());
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

    public static ParameterType get(Parameter parameter) {
        Class type = parameter.getType();
        Annotation[] annotations = parameter.getAnnotations();
        return get(type, annotations);
    }
}
