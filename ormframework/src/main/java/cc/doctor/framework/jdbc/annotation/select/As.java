package cc.doctor.framework.jdbc.annotation.select;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * select col as alias
 */
@Target(ElementType.FIELD)
@Retention(RUNTIME)
public @interface As {
    String value();
}
