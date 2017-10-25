package cc.doctor.framework.web.route;

import java.lang.reflect.Method;

/**
 * Created by doctor on 2017/7/17.
 */
public class RouteInvoke {
    private String service;
    private Class serviceClass;
    private String methodName;
    private Method method;
    private String exceptionHandler;
    private Boolean found;

    public String getService() {
        return service;
    }

    public void setService(String service) {
        this.service = service;
    }

    public Class getServiceClass() {
        return serviceClass;
    }

    public void setServiceClass(Class serviceClass) {
        this.serviceClass = serviceClass;
    }

    public String getMethodName() {
        return methodName;
    }

    public void setMethodName(String methodName) {
        this.methodName = methodName;
    }

    public void setMethod(Method method) {
        this.method = method;
    }

    public Method getMethod() {
        return method;
    }

    public String getExceptionHandler() {
        return exceptionHandler;
    }

    public void setExceptionHandler(String exceptionHandler) {
        this.exceptionHandler = exceptionHandler;
    }

    public Boolean getFound() {
        return found;
    }

    public void setFound(Boolean found) {
        this.found = found;
    }
}
