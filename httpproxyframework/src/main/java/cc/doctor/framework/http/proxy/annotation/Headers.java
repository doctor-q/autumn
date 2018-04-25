package cc.doctor.framework.http.proxy.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 添加http头部
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface Headers {
    /**
     * 使用的头部，* 表示所有
     */
    String[] includeHeaders() default {"*"};

    /**
     * 不使用的头部
     */
    String[] excludeHeaders() default {};
}
