package cc.doctor.framework.web.handler.parser;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * Created by doctor on 2017/7/21.
 * json 参数
 */
@Target(ElementType.PARAMETER)
@Retention(RUNTIME)
public @interface JsonParam {
}
