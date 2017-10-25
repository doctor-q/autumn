package cc.doctor.framework.jdbc.annotation.insert;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * if value is null, the column will be still insert
 */
@Target({ElementType.FIELD, ElementType.TYPE})
@Retention(RUNTIME)
public @interface InsertNotSkipNull {
}
