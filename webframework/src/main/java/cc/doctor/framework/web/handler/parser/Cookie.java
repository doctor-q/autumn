package cc.doctor.framework.web.handler.parser;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * Created by doctor on 2017/7/19.
 * 封装cookie值到参数或字段
 */
@Target({ElementType.PARAMETER, ElementType.FIELD})
@Retention(RUNTIME)
public @interface Cookie {
    /**
     * 别名
     */
    String value();
}
