package cc.doctor.framework.http.proxy;

public class MethodNotSupportException extends RuntimeException {
    public MethodNotSupportException() {
        super("Method not support.");
    }
}
