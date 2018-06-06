package cc.doctor.framework.web.handler.invoke;

public class InvalidParamException extends Exception {
    private static final long serialVersionUID = 940812693330801450L;

    public InvalidParamException(String message) {
        super(message);
    }
}
