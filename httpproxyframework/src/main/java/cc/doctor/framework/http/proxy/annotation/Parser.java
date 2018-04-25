package cc.doctor.framework.http.proxy.annotation;

import cc.doctor.framework.http.proxy.response.ResponseParser;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface Parser {
    Class<? extends ResponseParser> value();
}
