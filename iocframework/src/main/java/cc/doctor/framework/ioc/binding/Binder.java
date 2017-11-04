package cc.doctor.framework.ioc.binding;

import java.lang.annotation.Annotation;

public interface Binder {
    Binding bind(BindKey bindKey);

    Binding bind(RealType realType);

    Binding bind(Class clazz);

    Binding bind(Annotation annotation);
}
