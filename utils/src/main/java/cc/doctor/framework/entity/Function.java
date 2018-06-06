package cc.doctor.framework.entity;

/**
 * Created by doctor on 2017/7/30.
 */
public interface Function<F,T> {
     T transform(F from);
}
