package cc.doctor.framework.jdbc.annotation.update;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * if value is null, column will be still update
 */
@Target({ElementType.FIELD, ElementType.TYPE})
@Retention(RUNTIME)
public @interface UpdateNotSkipNull {
}
