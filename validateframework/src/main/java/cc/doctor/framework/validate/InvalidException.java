package cc.doctor.framework.validate;

/**
 * Created by doctor on 2017/12/19.
 * 不合法的参数异常
 */
public class InvalidException extends Exception {
    private static final long serialVersionUID = 6993857580647641427L;

    public InvalidException(String msg) {
        super(msg);
    }
}

