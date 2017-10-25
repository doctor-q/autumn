package cc.doctor.framework.jdbc.annotation.insert;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * Created by doctor on 2017/8/8.
 * copy a field value to another
 */
@Target(ElementType.FIELD)
@Retention(RUNTIME)
public @interface CopyTo {
}
