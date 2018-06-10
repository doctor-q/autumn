package cc.doctor.framework.web.utils;

import cc.doctor.framework.utils.Container;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import java.util.Set;

public class StringFormats {
    public static Object parse(Object param, Class type) {
        if (param == null) {
            return null;
        }
        if (!(param instanceof String)) {
            return param;
        }
        String parameter = param.toString();
        Object value = null;
        if (type.equals(String.class)) {
            return parameter;
        } else if (type.equals(Integer.class)) {
            value = Integer.parseInt(parameter);
        } else if (type.equals(Long.class)) {
            value = Long.parseLong(parameter);
        } else if (type.equals(Float.class)) {
            value = Float.parseFloat(parameter);
        } else if (type.equals(Double.class)) {
            value = Double.parseDouble(parameter);
        } else if (type.equals(BigDecimal.class)) {
            value = BigDecimal.valueOf(Double.parseDouble(parameter));
        } else if (type.equals(List.class)) {
            value = Arrays.asList(parameter.split(","));
        } else if (type.equals(Set.class)) {
            Set<String> set = (Set<String>) Container.container.newComponent(type);
            set.addAll(Arrays.asList(parameter.split(",")));
            value = set;

        } else if (type.equals(String.class)) {
            value = parameter;
        }
        return value;
    }
}
