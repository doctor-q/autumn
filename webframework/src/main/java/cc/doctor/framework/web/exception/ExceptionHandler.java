package cc.doctor.framework.web.exception;

/**
 * Created by doctor on 17-6-2.
 */
public interface ExceptionHandler<T> {
    T handleException(Throwable e);
}


