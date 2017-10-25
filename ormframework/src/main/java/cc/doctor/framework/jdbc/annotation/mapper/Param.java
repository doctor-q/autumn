package cc.doctor.framework.jdbc.annotation.mapper;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * 参数名，多个参数必须使用这个分开，单个参数不必
 */
@Target(ElementType.PARAMETER)
@Retention(RUNTIME)
public @interface Param {
    String value();
}
