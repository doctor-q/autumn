package cc.doctor.framework.validate;

import cc.doctor.framework.utils.ReflectUtils;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.util.Map;

public class ValidateService {
    public void validate(Object param) throws InvalidException {
        if (param == null) {
            return;
        }
        Map<String, Field> attrNameFields = ReflectUtils.getObjectAttrNameFields(param.getClass());
        for (Field field : attrNameFields.values()) {
            Annotation[] annotations = field.getAnnotations();
            for (Annotation annotation : annotations) {
                Validator validator = ValidatorRegistry.get(annotation.annotationType());
                if (validator != null) {
                    validator.validate(annotation, ReflectUtils.get(field.getName(), param));
                }
            }
        }
    }
}
