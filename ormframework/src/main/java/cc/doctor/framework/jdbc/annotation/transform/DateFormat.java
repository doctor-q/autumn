package cc.doctor.framework.jdbc.annotation.transform;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * Created by doctor on 2017/7/12.
 * format date to a string
 */
@Target(ElementType.FIELD)
@Retention(RUNTIME)
public @interface DateFormat {
    String pattern() default "yyyy-MM-dd HH:mm:ss";
}
