package cc.doctor.framework.web.handler.in;

import cc.doctor.framework.utils.ReflectUtils;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by doctor on 17-8-4.
 */
public class EnumDataMapperHolder {
    public static final EnumDataMapperHolder enumDataMapperHolder = new EnumDataMapperHolder();
    public Object enumTransfer(Field field, Object from) {
        if (field.isAnnotationPresent(EnumDataMapper.class)) {
            EnumDataMapper enumDataMapper = field.getAnnotation(EnumDataMapper.class);
            Map fromToMap = fromToMap(enumDataMapper);
            if (fromToMap != null) {
                return fromToMap.get(from);
            }
        }
        return null;
    }


    public Map fromToMap(EnumDataMapper enumDataMapper) {
        Map<Object, Object> fromToMap = new HashMap<>();
        String enumClass = enumDataMapper.enumClass();
        try {
            Class aClass = Class.forName(enumClass);
            Object[] enumMap = ReflectUtils.enumMap(aClass);
            if (enumMap == null) {
                return null;
            }
            for (Object enumEntity : enumMap) {
                Object from = ReflectUtils.get(enumDataMapper.fieldFrom(), enumEntity);
                Object to = ReflectUtils.get(enumDataMapper.fieldTo(), enumEntity);
                if (from != null) {
                    fromToMap.put(from, to);
                }
            }
            return fromToMap;
        } catch (ClassNotFoundException e) {
            return null;
        }
    }
}
