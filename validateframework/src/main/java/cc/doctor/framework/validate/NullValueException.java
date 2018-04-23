package cc.doctor.framework.validate;

public class NullValueException extends InvalidException {
    public NullValueException() {
        super("Value is null");
    }
}
