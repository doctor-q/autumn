package cc.doctor.framework.ioc.binding;

public interface Binder {
    Binding bind(BindKey bindKey);

    Binding bind(RealType realType);

    Binding bind(Class clazz);

    Binding withAnnotation(Class annotation);

    Binding to(Class clazz);

    <T> Binding to(T instance);
}
