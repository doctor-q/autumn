package cc.doctor.framework.web.route;

import cc.doctor.framework.utils.Container;
import cc.doctor.framework.web.aop.AopRegistry;
import cc.doctor.framework.web.handler.invoke.InvokeAop;
import cc.doctor.framework.web.handler.invoke.Invoker;
import cc.doctor.framework.web.handler.invoke.Parameter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;

/**
 * Created by doctor on 2017/7/17.
 */
public class RouteInvoke {
    private static final Logger log = LoggerFactory.getLogger(RouteInvoke.class);

    private String service;
    private Class serviceClass;
    private String methodName;
    private Method method;
    private String exceptionHandler;
    private Boolean found;
    private Invoker invoker;

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

    public Invoker getInvoker() {
        return invoker;
    }

    public void setInvoker(Invoker invoker) {
        this.invoker = invoker;
    }

    public Invoker getAndBuildInvoker() {
        if (invoker == null) {
            Invoker invoker = new Invoker();
            // instance
            Object instance = Container.container.getOrCreateComponent(serviceClass);
            invoker.setInstance(instance);
            // param annotation
            Annotation[][] parameterAnnotations = method.getParameterAnnotations();
            Class<?>[] parameterTypes = method.getParameterTypes();
            int paramLength = parameterTypes.length;
            for (int i = 0; i < paramLength; i++) {
                Parameter parameter = new Parameter(parameterTypes[i], parameterAnnotations[i]);
                invoker.addParameter(parameter);
            }
            // aop annotation
            Annotation[] methodAnnotations = method.getAnnotations();
            InvokeAop invokeAop = new InvokeAop();
            for (Annotation methodAnnotation : methodAnnotations) {
                if (AopRegistry.contains(methodAnnotation.annotationType())) {
                    invokeAop.addAop(methodAnnotation);
                }
            }
            this.invoker = invoker;
        }
        return invoker;
    }

    public Boolean getFound() {
        return found;
    }

    public void setFound(Boolean found) {
        this.found = found;
    }
}
