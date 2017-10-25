package cc.doctor.framework.thrift;

/**
 * Created by doctor on 2017/7/15.
 */
public interface MessageHandler {
    Response handle(String method, String param);
}
