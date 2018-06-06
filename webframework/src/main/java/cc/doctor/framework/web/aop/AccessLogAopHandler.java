package cc.doctor.framework.web.aop;

import cc.doctor.framework.web.handler.invoke.Parameter;
import com.alibaba.fastjson.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.annotation.Annotation;
import java.util.List;

public class AccessLogAopHandler implements AopHandler {
    private static final Logger log = LoggerFactory.getLogger(AccessLogAopHandler.class);

    @Override
    public void before(List<Parameter> parameters, Annotation logAccess) {

        String param = JSONObject.toJSONString(parameters);
        log.info(param);
    }

    @Override
    public void after(Object invoke, Annotation logAccess) {
        String response = JSONObject.toJSONString(invoke);
        log.info(response);
    }
}
