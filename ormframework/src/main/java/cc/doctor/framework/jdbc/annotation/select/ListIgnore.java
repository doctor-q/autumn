package cc.doctor.framework.jdbc.annotation.select;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * Created by doctor on 2017/7/16.
 * field ignore on select list
 */
@Target(ElementType.FIELD)
@Retention(RUNTIME)
public @interface ListIgnore {
}
