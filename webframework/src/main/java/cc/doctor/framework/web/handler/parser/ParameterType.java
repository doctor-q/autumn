package cc.doctor.framework.web.handler.parser;

import cc.doctor.framework.utils.Container;
import cc.doctor.framework.utils.ReflectUtils;
import cc.doctor.framework.web.handler.in.RequestAnnotationHandler;
import cc.doctor.framework.web.handler.in.RequestHandlerFactory;
import cc.doctor.framework.web.handler.invoke.Parameter;
import cc.doctor.framework.web.servlet.meta.HttpMetadata;
import cc.doctor.framework.web.utils.StringFormats;
import com.alibaba.fastjson.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.util.Map;

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

            // unpack can do a before after hook
            Object instance = Container.container.newComponent(parameter.getType());
            parameter.setValue(instance);
            ((Unpack) instance).beforeUnpack(httpMetadata);
            fillObject(httpMetadata, parameter);
            ((Unpack) instance).afterUnpack(httpMetadata);

        }
    },
    JSON(null, JsonParam.class) {
        @Override
        public void setParameter(Parameter parameter, HttpMetadata httpMetadata) {
            Object object = JSONObject.parseObject(httpMetadata.getJson(), parameter.getType());
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
            parameter.setValue(StringFormats.parse(value, parameter.getType()));
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
            parameter.setValue(StringFormats.parse(value, parameter.getType()));
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
            parameter.setValue(StringFormats.parse(value, parameter.getType()));
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
            parameter.setValue(StringFormats.parse(value, parameter.getType()));
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
                    parameterType.paramType.isAssignableFrom(paramType)) {
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
        // override do nothing
    }

    public Annotation getAnnotation(Annotation[] annotations, Class annotationClass) {
        for (Annotation annotation : annotations) {
            if (annotation.annotationType().equals(annotationClass)) {
                return annotation;
            }
        }
        return null;
    }

    public void fillObject(HttpMetadata httpMetadata, Parameter parameter) {
        if (parameter.getValue() == null) {
            parameter.setValue(Container.container.newComponent(parameter.getType()));
        }
        Map<String, Field> objectAttrNameFields = ReflectUtils.getObjectAttrNameFields(parameter.getType());
        for (String name : objectAttrNameFields.keySet()) {
            Field field = objectAttrNameFields.get(name);
            // 仅仅封装为空的参数
            Object fieldValue = ReflectUtils.get(field.getName(), parameter.getValue());
            if (fieldValue != null) {
                continue;
            }
            // 提取请求的参数
            String param = ParameterSource.getParameterOfAll(httpMetadata, field);
            //do annotation
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
            Object value = param;
            // 请求注解转换
            Annotation[] annotations = field.getAnnotations();
            for (Annotation annotation : annotations) {
                RequestAnnotationHandler requestAnnotationHandler = RequestHandlerFactory.getAnnotationHandler(annotation.getClass());
                if (requestAnnotationHandler != null) {
                    value = requestAnnotationHandler.handler(value.toString(), annotation);
                }
            }
            // 基本类型转换
            value = StringFormats.parse(value, field.getType());
            if (value != null) {
                ReflectUtils.set(field, value, parameter.getValue());
            }
        }
    }

    public static ParameterType get(Parameter parameter) {
        Class type = parameter.getType();
        Annotation[] annotations = parameter.getAnnotations();
        return get(type, annotations);
    }
}
