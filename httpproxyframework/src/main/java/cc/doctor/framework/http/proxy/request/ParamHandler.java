package cc.doctor.framework.http.proxy.request;

import cc.doctor.framework.entity.Tuple;
import cc.doctor.framework.utils.ReflectUtils;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;

public class ParamHandler {
    private static ParamHandler instance;

    private ParamHandler() {
    }

    public static ParamHandler getInstance() {
        if (instance == null) {
            instance = new ParamHandler();
        }
        return instance;
    }

    /**
     * convert param object to param tuple
     *
     * @param param a param instance
     * @return a tuple contains a get param map and a post param map
     */
    public ParamTuple createParamTuple(Object param) {
        ParamTuple paramTuple = new ParamTuple();
        Map<String, Field> attrNameFields = ReflectUtils.getObjectAttrNameFields(param.getClass());
        for (String name : attrNameFields.keySet()) {
            Object obj = ReflectUtils.get(name, param);
            if (obj == null) {
                continue;
            }
            Field field = attrNameFields.get(name);

        }
        return paramTuple;
    }
}
