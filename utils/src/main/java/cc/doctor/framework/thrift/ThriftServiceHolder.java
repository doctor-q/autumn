package cc.doctor.framework.thrift;

import org.apache.thrift.TException;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Created by doctor on 2017/7/15.
 */
public class ThriftServiceHolder implements ThriftService.Iface {
    private Map<String, MessageHandler> messageHandlerMap = new LinkedHashMap<>();
    @Override
    public Response invoke(Request request) throws TException {
        String serviceName = request.getServiceName();
        if (serviceName == null) {
            return ResponseUtils.errorResponse("Service name miss.");
        }
        String method = request.getMethod();
        String param = request.getParam();
        Response ret = null;
        for (String handlerName : messageHandlerMap.keySet()) {
            if (serviceName.equals(handlerName)) {
                Response response = messageHandlerMap.get(handlerName).handle(method, param);
                if (response != null) {
                    ret = response;
                }
            }
        }
        return ret;
    }

    public void registerMessageHandler(MessageHandler messageHandler) {
        messageHandlerMap.put(messageHandler.getClass().getName(), messageHandler);
    }

    public void registerMessageHandler(String name, MessageHandler messageHandler) {
        messageHandlerMap.put(name, messageHandler);
    }
}
