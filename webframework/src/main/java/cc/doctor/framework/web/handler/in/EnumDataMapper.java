package cc.doctor.framework.web.handler.in;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * Created by doctor on 17-8-4.
 */
@Target(ElementType.FIELD)
@Retention(RUNTIME)
public @interface EnumDataMapper {
    //枚举类
    String enumClass();
    //枚举类字段，from
    String fieldFrom();
    //枚举类字段，to
    String fieldTo();
}
