package cc.doctor.framework.ioc.binding;

import java.lang.annotation.Annotation;

public class BindKey {
    private RealType realType;
    private Annotation annotation;

    public BindKey(RealType realType) {
        this.realType = realType;
    }

    public BindKey(Class clazz) {
        this.realType = new RealType(clazz);
    }

    public BindKey(Annotation annotation) {
        this.annotation = annotation;
    }

    public RealType getRealType() {
        return realType;
    }

    public void setRealType(RealType realType) {
        this.realType = realType;
    }

    public Annotation getAnnotation() {
        return annotation;
    }

    public void setAnnotation(Annotation annotation) {
        this.annotation = annotation;
    }

    public static BindKey get(Class clazz) {
        return null;
    }
}
