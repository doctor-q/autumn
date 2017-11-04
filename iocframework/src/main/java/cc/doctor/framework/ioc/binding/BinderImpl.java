package cc.doctor.framework.ioc.binding;

import java.lang.annotation.Annotation;

public class BinderImpl implements Binder {
    @Override
    public Binding bind(BindKey bindKey) {
        LinkedBinding linkedBinding = new LinkedBinding();
        linkedBinding.setBindKey(bindKey);
        return linkedBinding;
    }

    @Override
    public Binding bind(RealType realType) {
        LinkedBinding linkedBinding = new LinkedBinding();
        linkedBinding.setBindKey(new BindKey(realType));
        return linkedBinding;
    }

    @Override
    public Binding bind(Class clazz) {
        LinkedBinding linkedBinding = new LinkedBinding();
        linkedBinding.setBindKey(new BindKey(clazz));
        return linkedBinding;
    }

    @Override
    public Binding bind(Annotation annotation) {
        LinkedBinding linkedBinding = new LinkedBinding();
        linkedBinding.setBindKey(new BindKey(annotation));
        return linkedBinding;
    }
}
