package cc.doctor.framework.ioc.binding;

import cc.doctor.framework.ioc.Injector;
import cc.doctor.framework.ioc.ProviderPoint;

public class LinkedBinding extends Binding {
    private Injector injector;
    private BindKey target;

    public BindKey getTarget() {
        return target;
    }

    public void setTarget(BindKey target) {
        this.target = target;
    }

    @Override
    public Binding to(Class clazz) {
        this.target = new BindKey(clazz);
        return this;
    }

    @Override
    public <T> T getInstance(Class<T> tClass) {
        BindKey bindKey = BindKey.get(tClass);
        Binding binding;
        while ((binding = injector.getBinding(bindKey)) != null) {
            if (binding instanceof LinkedBinding && ((LinkedBinding) binding).getTarget() != null) {
                bindKey = ((LinkedBinding) binding).getTarget();
            } else {
                if (binding.getProviderPoint() == null) {
                    throw new RuntimeException("Binding not complete.");
                }
                ProviderPoint providerPoint = binding.getProviderPoint();
                return (T) providerPoint.provide();
            }
        }
        return null;
    }
}
