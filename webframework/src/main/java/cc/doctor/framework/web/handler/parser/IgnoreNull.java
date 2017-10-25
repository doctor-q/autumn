package cc.doctor.framework.web.handler.parser;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * Created by doctor on 17-6-9.
 * don't do set if parameter is null
 */
@Target({ElementType.TYPE, ElementType.FIELD})
@Retention(RUNTIME)
public @interface IgnoreNull {
}
