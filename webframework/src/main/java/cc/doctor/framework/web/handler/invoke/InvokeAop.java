package cc.doctor.framework.web.handler.invoke;


import cc.doctor.framework.web.aop.AopHandler;
import cc.doctor.framework.web.aop.AopRegistry;

import java.lang.annotation.Annotation;
import java.util.LinkedList;
import java.util.List;

public class InvokeAop {
    private List<Annotation> annotations = new LinkedList<>();
    public void beforeChain(List<Parameter> parameters) throws Throwable {
        for (Annotation annotation : annotations) {
            AopHandler aopHandler = AopRegistry.getAopHandler(annotation);
            aopHandler.before(parameters, annotation);
        }
    }

    public void afterChain(Object invoke) throws Throwable {
        for (Annotation annotation : annotations) {
            AopHandler aopHandler = AopRegistry.getAopHandler(annotation);
            aopHandler.after(invoke, annotation);
        }
    }

    public void addAop(Annotation annotation) {
        annotations.add(annotation);
    }
}
