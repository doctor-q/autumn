package cc.doctor.framework.web.handler.in;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * Created by doctor on 17-6-9.
 * format string value to date
 */
@Target(ElementType.FIELD)
@Retention(RUNTIME)
public @interface DateParse {
    String pattern() default "yyyy-MM-dd HH:mm:ss";
}
