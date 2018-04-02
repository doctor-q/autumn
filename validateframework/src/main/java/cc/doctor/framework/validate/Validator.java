package cc.doctor.framework.validate;


import java.lang.annotation.Annotation;

/**
 * Created by doctor on 2017/11/24.
 */
public interface Validator<A extends Annotation> {

    boolean validate(A annotation, String field, Object value) throws InvalidException;

}

