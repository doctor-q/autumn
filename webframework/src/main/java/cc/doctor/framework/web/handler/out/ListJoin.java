package cc.doctor.framework.web.handler.out;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * Created by doctor on 2017/7/21.
 */
@Target(ElementType.FIELD)
@Retention(RUNTIME)
public @interface ListJoin {
    String on() default ",";
}
