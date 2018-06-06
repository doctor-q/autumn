package cc.doctor.framework.web.handler.invoke;

import cc.doctor.framework.web.handler.parser.Form;
import cc.doctor.framework.web.handler.parser.Unpack;

import java.lang.annotation.Annotation;

/**
 * 方法参数的抽象
 */
public class Parameter {
    private Class type;
    private Annotation[] annotations;
    private Object value;
    /**
     * 是否简单参数
     */
    private Boolean isSimple;

    public Parameter(Class type, Annotation[] annotations) {
        this.annotations = annotations;
        this.type = type;
    }

    public Annotation[] getAnnotations() {
        return annotations;
    }

    public void setAnnotations(Annotation[] annotations) {
        this.annotations = annotations;
    }

    public Class getType() {
        return type;
    }

    public void setType(Class type) {
        this.type = type;
    }

    public Object getValue() {
        return value;
    }

    public void setValue(Object value) {
        this.value = value;
    }

    public Boolean isSimple() {
        if (isSimple == null) {
            if (Unpack.class.isAssignableFrom(type)) {
                isSimple = false;
            } else {
                for (Annotation annotation : annotations) {
                    if (annotation instanceof Form) {
                        isSimple = false;
                        break;
                    }
                }
            }
        }
        return isSimple;
    }

    public void setSimple(Boolean simple) {
        isSimple = simple;
    }
}
