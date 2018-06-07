package cc.doctor.framework.web.exception;


/**
 * Created by doctor on 17-6-2.
 */
public class SimpleResultExceptionHandler implements ExceptionHandler<String> {

    @Override
    public String handleException(Throwable e) {
        return e.getCause().toString();
    }
}
