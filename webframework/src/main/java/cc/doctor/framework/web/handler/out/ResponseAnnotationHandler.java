package cc.doctor.framework.web.handler.out;

/**
 * Created by doctor on 2017/7/19.
 */
public interface ResponseAnnotationHandler<A> {
    Object handler(Object origin, A annotation);
}
