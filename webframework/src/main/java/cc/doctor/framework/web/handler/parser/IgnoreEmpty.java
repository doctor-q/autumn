package cc.doctor.framework.web.handler.parser;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * Created by doctor on 17-6-9.
 * ignore empty parameter, if trim is true, trim parameter first.
 */
@Target({ElementType.TYPE, ElementType.FIELD})
@Retention(RUNTIME)
public @interface IgnoreEmpty {
    boolean trim() default true;
}
