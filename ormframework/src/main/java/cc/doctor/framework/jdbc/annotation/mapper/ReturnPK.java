package cc.doctor.framework.jdbc.annotation.mapper;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * Created by doctor on 2017/8/7.
 * return primary key or not
 */
@Target(ElementType.METHOD)
@Retention(RUNTIME)
public @interface ReturnPK {
}
