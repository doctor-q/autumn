package cc.doctor.framework.web.handler.in;

/**
 * Created by doctor on 17-6-9.
 */
public interface RequestAnnotationHandler<A> {
    Object handler(String parameter, A annotation);
}
