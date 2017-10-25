package cc.doctor.framework.web.handler.parser;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * Created by doctor on 2017/7/19.
 * Give up java assist, route method param must use it if is not unpack.
 */
@Target(ElementType.PARAMETER)
@Retention(RUNTIME)
public @interface Param {
    String value();
}
