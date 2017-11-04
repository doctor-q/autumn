package cc.doctor.framework.ioc;

public enum Scope {
    SINGLETON("singleton"), THREAD("thread"), PROTOTYPE("prototype");

    private String scope;

    Scope(String scope) {
        this.scope = scope;
    }

    public String getScope() {
        return scope;
    }
}
