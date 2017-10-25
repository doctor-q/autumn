package cc.doctor.framework.jdbc.annotation.transform;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * Created by doctor on 2017/8/7.
 * do decrypt operation after select
 */
@Target(ElementType.FIELD)
@Retention(RUNTIME)
public @interface Decrypt {
    String type() default "aes";
    String secretKey() default "";
}
