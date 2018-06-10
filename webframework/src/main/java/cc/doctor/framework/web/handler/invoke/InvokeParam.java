package cc.doctor.framework.web.handler.invoke;


import cc.doctor.framework.entity.Tuple;
import cc.doctor.framework.utils.Container;
import cc.doctor.framework.utils.ReflectUtils;
import cc.doctor.framework.web.handler.in.ListSplit;
import cc.doctor.framework.web.handler.parser.Form;
import cc.doctor.framework.web.handler.parser.Param;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * 执行参数
 */
public class InvokeParam {
    public static final Logger log = LoggerFactory.getLogger(InvokeParam.class);

    /**
     * 参数类型列表
     */
    private List<Class> paramTypes;
    /**
     * 参数注解列表
     */
    private List<Annotation> annotations;
    /**
     * 简单参数列表
     */
    private Map<String, Tuple<Class, Integer>> simpleParams = new LinkedHashMap<>();
    /**
     * 复合参数列表
     */
    private Map<Class, Tuple<Map<String, Field>, Integer>> combineAttrFields = new LinkedHashMap<>();
    /**
     * 参数列表
     */
    private List params = new LinkedList();

    public List<Class> getParamTypes() {
        return paramTypes;
    }

    public List<Annotation> getAnnotations() {
        return annotations;
    }

    public Map<String, Tuple<Class, Integer>> getSimpleParams() {
        return simpleParams;
    }

    public Map<Class, Tuple<Map<String, Field>, Integer>> getCombineAttrFields() {
        return combineAttrFields;
    }

    public List getParams() {
        return params;
    }

    public InvokeParam(List<Class> paramTypes, List<Annotation> annotations) {
        this.paramTypes = paramTypes;
        this.annotations = annotations;
        createParamMap();
    }

    private void createParamMap() {
        int length = paramTypes.size();
        for (int i = 0; i < length; i++) {
            Class aClass = paramTypes.get(i);
            params.add(Container.container.getOrCreateComponent(aClass));
            Annotation annotation = annotations.get(i);
            if (annotation instanceof Param) {
                String key = ((Param) annotation).value();
                simpleParams.put(key, new Tuple<>(aClass, i));
            } else if (annotation instanceof Form) {
                combineAttrFields.put(aClass, new Tuple<>(ReflectUtils.getObjectAttrNameFields(aClass), i));
            }
        }
    }

    /**
     * 设置参数，按照如下优先级
     * 1. 简单参数，使用{@link Param}注解标注的
     * 2. 复合参数从前往后的顺序一次匹配
     *
     * @param key   参数名
     * @param value 参数值
     */
    public void setValue(String key, Object value) throws InvalidParamException {
        if (simpleParams.containsKey(key)) {
            Tuple<Class, Integer> classIntegerTuple = simpleParams.get(key);
            Class aClass = classIntegerTuple.getT1();
            Integer index = classIntegerTuple.getT2();
            params.set(index, transformValue(aClass, value));
            return;
        }
        for (Class aClass : combineAttrFields.keySet()) {
            Tuple<Map<String, Field>, Integer> mapIntegerTuple = combineAttrFields.get(aClass);
            Map<String, Field> fieldMap = mapIntegerTuple.getT1();
            Integer index = mapIntegerTuple.getT2();
            if (fieldMap.containsKey(key)) {
                Field field = fieldMap.get(key);
                Annotation[] annotations = field.getAnnotations();
                for (Annotation annotation : annotations) {
                    if (ParamValidator.contains(annotation.annotationType())) {
                        ParamValidator.get(annotation.annotationType()).validate(field.getName(), value);
                    }
                }

                if (value == null) {
                    continue;
                }
                if (field.isAnnotationPresent(ListSplit.class)) {
                    ListSplit listSplit = field.getAnnotation(ListSplit.class);
                    Class generic = listSplit.generic();
                    String on = listSplit.separator();
                    String[] split = value.toString().split(on);
                    List list = new LinkedList();
                    for (String s : split) {
                        list.add(transformValue(generic, s));
                    }
                    ReflectUtils.set(field, list, params.get(index));
                } else {
                    ReflectUtils.set(field, transformValue(field.getType(), value), params.get(index));
                }
            }
        }
    }

    public Object getValue(String key) {
        if (simpleParams.containsKey(key)) {
            Integer index = simpleParams.get(key).getT2();
            return params.get(index);
        }
        for (Class combineClass : combineAttrFields.keySet()) {
            Tuple<Map<String, Field>, Integer> mapIntegerTuple = combineAttrFields.get(combineClass);
            Map<String, Field> fieldMap = mapIntegerTuple.getT1();
            if (fieldMap.containsKey(key)) {
                Integer index = mapIntegerTuple.getT2();
                Object combine = params.get(index);
                return ReflectUtils.get(key, combine);
            }
        }
        return null;
    }

    private Object transformValue(Class aClass, Object value) {
        if (value == null) {
            return null;
        }
        Object transform = null;
        if (aClass.equals(String.class)) {
            transform = value.toString();
        } else if (aClass.equals(Integer.class)) {
            transform = Integer.parseInt(value.toString());
        } else if (aClass.equals(Long.class)) {
            transform = Long.parseLong(value.toString());
        } else if (aClass.equals(Short.class)) {
            transform = Short.parseShort(value.toString());
        } else if (aClass.equals(Double.class)) {
            transform = Double.parseDouble(value.toString());
        } else if (aClass.equals(Float.class)) {
            transform = Float.parseFloat(value.toString());
        } else if (aClass.equals(BigDecimal.class)) {
            transform = BigDecimal.valueOf(Double.parseDouble(value.toString()));
        } else if (aClass.equals(Byte.class)) {
            transform = Byte.parseByte(value.toString());
        } else if (aClass.equals(Character.class)) {
            transform = value.toString().charAt(0);
        }
        return transform;
    }
}
