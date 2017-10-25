package cc.doctor.framework.jdbc.annotation.update;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * Created by doctor on 2017/8/8.
 * never update this field
 */
@Target(ElementType.FIELD)
@Retention(RUNTIME)
public @interface UpdateIgnore {
}
