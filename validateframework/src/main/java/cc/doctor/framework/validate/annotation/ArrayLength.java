package cc.doctor.framework.validate.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Created by doctor on 2017/11/24.
 * 验证数组（集合）长度
 */
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface ArrayLength {
    int min() default 0;
    int max() default Integer.MAX_VALUE;
}
