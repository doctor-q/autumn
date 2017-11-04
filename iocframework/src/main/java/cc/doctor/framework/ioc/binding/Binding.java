package cc.doctor.framework.ioc.binding;

import cc.doctor.framework.ioc.ProviderPoint;


public abstract class Binding {
    private BindKey bindKey;
    private ProviderPoint providerPoint;

    public BindKey getBindKey() {
        return bindKey;
    }

    public void setBindKey(BindKey bindKey) {
        this.bindKey = bindKey;
    }

    public ProviderPoint getProviderPoint() {
        return providerPoint;
    }

    public void setProviderPoint(ProviderPoint providerPoint) {
        this.providerPoint = providerPoint;
    }

    public abstract Binding to(Class clazz);

    public abstract <T> T getInstance(Class<T> tClass);

    public Binding to(ProviderPoint providerPoint) {
        this.providerPoint = providerPoint;
        return this;
    }

    <T> Binding to(T instance) {
        this.providerPoint = ProviderPoint.instancePoint(instance);
        return this;
    }
}
