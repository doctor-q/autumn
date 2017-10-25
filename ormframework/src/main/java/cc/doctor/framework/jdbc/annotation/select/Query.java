package cc.doctor.framework.jdbc.annotation.select;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * Created by doctor on 17-8-4.
 * query in where clause
 */
@Target(ElementType.FIELD)
@Retention(RUNTIME)
public @interface Query {
    String field() default "";
    boolean reverse() default false;
}
