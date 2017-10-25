package cc.doctor.framework.jdbc.annotation.select;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * null as query condition, use in a query model or a query field
 */
@Target({ElementType.FIELD, ElementType.TYPE})
@Retention(RUNTIME)
public @interface SelectNotSkipNull {
}
