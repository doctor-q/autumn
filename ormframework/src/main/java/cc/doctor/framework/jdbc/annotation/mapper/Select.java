package cc.doctor.framework.jdbc.annotation.mapper;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;
import java.util.Map;

import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * Created by doctor on 2017/8/13.
 */
@Target(ElementType.METHOD)
@Retention(RUNTIME)
public @interface Select {
    String sql() default "select 1";
    Class resultType() default Map.class;
}
