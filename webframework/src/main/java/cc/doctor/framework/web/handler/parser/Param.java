package cc.doctor.framework.web.handler.parser;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * Created by doctor on 2017/7/19.
 * 封装参数Parameter到参数或字段
 */
@Target({ElementType.PARAMETER, ElementType.FIELD})
@Retention(RUNTIME)
public @interface Param {
    /**
     * 别名
     */
    String value() default "";
}
