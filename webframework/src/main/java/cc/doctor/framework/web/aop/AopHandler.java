package cc.doctor.framework.web.aop;


import cc.doctor.framework.web.handler.invoke.Parameter;

import java.lang.annotation.Annotation;
import java.util.List;

/**
 * Created by doctor on 2018/1/21.
 */
public interface AopHandler {
    void before(List<Parameter> parameters, Annotation annotation) throws Throwable;

    void after(Object invoke, Annotation annotation) throws Throwable;
}
