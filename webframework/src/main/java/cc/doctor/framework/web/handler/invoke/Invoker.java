package cc.doctor.framework.web.handler.invoke;

import cc.doctor.framework.entity.Function;
import cc.doctor.framework.utils.CollectionUtils;
import cc.doctor.framework.web.handler.parser.ParameterType;
import cc.doctor.framework.web.servlet.meta.HttpMetadata;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Method;
import java.util.LinkedList;
import java.util.List;


public class Invoker {
    private static final Logger log = LoggerFactory.getLogger(Invoker.class);

    /**
     * 执行类
     */
    private Object instance;
    /**
     * 执行方法
     */
    private Method method;
    /**
     * 执行参数
     */
    private List<Parameter> parameters = new LinkedList<>();

    /**
     * 执行切面
     */
    private InvokeAop invokeAop;

    public Invoker() {

    }

    public Object getInstance() {
        return instance;
    }

    public Method getMethod() {
        return method;
    }

    public InvokeAop getInvokeAop() {
        return invokeAop;
    }

    public void setInstance(Object instance) {
        this.instance = instance;
    }

    public void setMethod(Method method) {
        this.method = method;
    }

    public List<Parameter> getParameters() {
        return parameters;
    }

    public void setParameters(List<Parameter> parameters) {
        this.parameters = parameters;
    }

    public void setInvokeAop(InvokeAop invokeAop) {
        this.invokeAop = invokeAop;
    }

    public Object invoke(HttpMetadata httpMetadata) throws Throwable {
        // 执行aop
        if (invokeAop != null) {
            invokeAop.beforeChain(parameters);
        }
        // 设置参数值
        for (Parameter parameter : parameters) {
            ParameterType parameterType = ParameterType.get(parameter);
            parameterType.setParameter(parameter, httpMetadata);
        }
        Object invoke = method.invoke(instance, CollectionUtils.transform(parameters, new Function<Parameter, Object>() {
            @Override
            public Object transform(Parameter from) {
                return from.getValue();
            }
        }).toArray());
        // 执行aop
        if (invokeAop != null) {
            invokeAop.afterChain(invoke);
        }
        return invoke;
    }

    public void addParameter(Parameter parameter) {
        parameters.add(parameter);
    }
}
