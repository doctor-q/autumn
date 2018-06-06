package cc.doctor.framework.web.exception;

public @interface ExceptionHandlers {
    Class<? extends ExceptionHandler>[] value() default SimpleResultExceptionHandler.class;
}
