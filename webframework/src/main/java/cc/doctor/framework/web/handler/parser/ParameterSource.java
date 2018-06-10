package cc.doctor.framework.web.handler.parser;

import cc.doctor.framework.web.servlet.meta.HttpMetadata;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;

public enum ParameterSource {
    PARAMETER(Param.class) {
        @Override
        public String getParameter(HttpMetadata httpMetadata, Field field) {
            String param = null;
            Param paramAnno = field.getAnnotation(Param.class);
            if (!paramAnno.value().trim().isEmpty()) {
                param = httpMetadata.getParam(paramAnno.value());
            } else {
                param = httpMetadata.getParam(field.getName());
            }
            return param;
        }
    },
    ATTRIBUTE(Attribute.class) {
        @Override
        public String getParameter(HttpMetadata httpMetadata, Field field) {
            String param = null;
            Attribute attributeAnno = field.getAnnotation(Attribute.class);
            if (!attributeAnno.value().trim().isEmpty()) {
                param = httpMetadata.getAttribute(attributeAnno.value());
            } else {
                param = httpMetadata.getAttribute(field.getName());
            }
            return param;
        }
    },
    HEADER(Header.class) {
        @Override
        public String getParameter(HttpMetadata httpMetadata, Field field) {
            String param = null;
            Header headerAnno = field.getAnnotation(Header.class);
            if (!headerAnno.value().trim().isEmpty()) {
                param = httpMetadata.getHeader(headerAnno.value());
            } else {
                param = httpMetadata.getHeader(field.getName());
            }
            return param;
        }
    },
    COOKIE(Cookie.class) {
        @Override
        public String getParameter(HttpMetadata httpMetadata, Field field) {
            String param = null;
            Cookie cookieAnno = field.getAnnotation(Cookie.class);
            if (!cookieAnno.value().trim().isEmpty()) {
                param = httpMetadata.getCookie(cookieAnno.value());
            } else {
                param = httpMetadata.getCookie(field.getName());
            }
            return param;
        }
    };
    private Class<? extends Annotation> annotationClass;

    ParameterSource(Class<? extends Annotation> annotationClass) {
        this.annotationClass = annotationClass;
    }

    /**
     * 从各自的源提取参数
     *
     * @param httpMetadata http请求的元数据
     * @param field        字段签名
     * @return 提取的参数
     */
    public String getParameter(HttpMetadata httpMetadata, Field field) {
        return null;
    }

    public static String getParameterOfAll(HttpMetadata httpMetadata, Field field) {
        for (ParameterSource parameterSource : ParameterSource.values()) {
            if (field.isAnnotationPresent(parameterSource.annotationClass)) {
                return parameterSource.getParameter(httpMetadata, field);
            }
        }
        return httpMetadata.getParam(field.getName());
    }
}
