package cc.doctor.framework.web.handler.in;


import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * Created by doctor on 17-6-10.
 * 数据映射
 */
@Target(ElementType.FIELD)
@Retention(RUNTIME)
public @interface DataMapper {
    String[] froms();
    String[] tos();
}
