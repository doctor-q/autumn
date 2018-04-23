package cc.doctor.framework.validate;


import java.lang.annotation.Annotation;

/**
 * Created by doctor on 2017/11/24.
 */
public interface Validator<A extends Annotation> {

    void validate(A annotation, Object value) throws InvalidException;
}

