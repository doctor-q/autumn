package cc.doctor.framework.ioc;

public class ProviderPoint extends Point {
    private Object instance;
    private Scope scope;

    public Object getInstance() {
        return instance;
    }

    public void setInstance(Object instance) {
        this.instance = instance;
    }

    public Scope getScope() {
        return scope;
    }

    public void setScope(Scope scope) {
        this.scope = scope;
    }

    public static ProviderPoint instancePoint(Object instance) {
        ProviderPoint providerPoint = new ProviderPoint();
        providerPoint.setInstance(instance);
        return providerPoint;
    }

    public Object provide() {
        return instance;
    }
}
