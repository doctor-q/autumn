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

    // 路由路径
    private String path;
    // 控制器别名
    private String controller;
    // 控制器类型
    private Class controllerClass;
    // 方法名
    private String methodName;
    // 方法
    private Method method;
    // 异常处理器
    private Class exceptionHandler;
    private Boolean found;
    // 方法执行参数、切面
    private Invoker invoker;

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getController() {
        return controller;
    }

    public void setController(String controller) {
        this.controller = controller;
    }

    public Class getControllerClass() {
        return controllerClass;
    }

    public void setControllerClass(Class controllerClass) {
        this.controllerClass = controllerClass;
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

    public Class getExceptionHandler() {
        return exceptionHandler;
    }

    public void setExceptionHandler(Class exceptionHandler) {
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
            Object instance = Container.container.getOrCreateComponent(controllerClass);
            invoker.setInstance(instance);
            invoker.setMethod(method);
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
