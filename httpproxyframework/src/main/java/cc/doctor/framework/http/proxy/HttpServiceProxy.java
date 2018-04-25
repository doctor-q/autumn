package cc.doctor.framework.http.proxy;

import cc.doctor.framework.http.proxy.annotation.BaseUrl;
import cc.doctor.framework.http.proxy.annotation.Headers;
import cc.doctor.framework.http.proxy.annotation.Parser;
import cc.doctor.framework.http.proxy.header.HeaderStore;
import cc.doctor.framework.http.proxy.request.ParamHandler;
import cc.doctor.framework.http.proxy.request.ParamTuple;
import cc.doctor.framework.http.proxy.response.ResponseParser;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.*;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by doctor on 2017/11/24.
 * http service 动态代理
 */
public class HttpServiceProxy implements InvocationHandler {
    public static final Logger log = LoggerFactory.getLogger(HttpServiceProxy.class);
    private static Map<String, HttpServiceProxy> httpServiceProxyMap = new ConcurrentHashMap<>();
    private String baseUrl;
    private ResponseParser responseParser;
    private HeaderStore headerStore;

    public HttpServiceProxy(String baseUrl) {
        this.baseUrl = baseUrl;
    }

    public static HttpServiceProxy get(String baseUrl) {
        if (!httpServiceProxyMap.containsKey(baseUrl)) {
            httpServiceProxyMap.put(baseUrl, new HttpServiceProxy(baseUrl));
        }
        return httpServiceProxyMap.get(baseUrl);
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        Object param = args[0];
        Class<?> returnType = method.getReturnType();
        CloseableHttpResponse httpResponse = invoke(method, param);
        return responseParser.parseHttpResponse(returnType, httpResponse);
    }

    public <T> T create(Class<T> service) {
        String base = "";
        ResponseParser responseParser = ResponseParser.defaultResponseParser();
        if (service.isAnnotationPresent(BaseUrl.class)) {
            BaseUrl rootUrl = service.getAnnotation(BaseUrl.class);
            base = rootUrl.value();
        }
        HttpServiceProxy httpServiceProxy = get(base);
        if (service.isAnnotationPresent(Parser.class)) {
            Parser parser = service.getAnnotation(Parser.class);
            try {
                Constructor<? extends ResponseParser> constructor = parser.value().getConstructor();
                responseParser = constructor.newInstance();
            } catch (Exception e) {
                log.error("", e);
            }
        }
        httpServiceProxy.setResponseParser(responseParser);
        if (service.isAnnotationPresent(cc.doctor.framework.http.proxy.annotation.HeaderStore.class)) {
            cc.doctor.framework.http.proxy.annotation.HeaderStore store = service.getAnnotation(cc.doctor.framework.http.proxy.annotation.HeaderStore.class);
            try {
                Constructor<? extends HeaderStore> constructor = store.value().getConstructor();
                httpServiceProxy.setHeaderStore(constructor.newInstance());
            } catch (Exception e) {
                log.error("", e);
            }
        }
        Class[] classes = {service};
        return (T) Proxy.newProxyInstance(HttpServiceProxy.class.getClassLoader(), classes, httpServiceProxy);
    }

    public CloseableHttpResponse invoke(Method method, Object param) throws Exception {
        MethodInvoker methodInvoker = MethodInvokerFactory.getInstance().methodInvoker(method);
        ParamTuple paramTuple = ParamHandler.getInstance().createParamTuple(param);
        methodInvoker.setParamTuple(paramTuple);
        if (method.isAnnotationPresent(Headers.class)) {
            Headers heads = method.getAnnotation(Headers.class);
            Map<String, String> headers = headerStore.getHeaders(heads.includeHeaders(), heads.excludeHeaders());
            methodInvoker.setHeaders(headers);
        }
        return methodInvoker.invoke();
    }

    public void setBaseUrl(String baseUrl) {
        this.baseUrl = baseUrl;
    }

    public String getBaseUrl() {
        return baseUrl;
    }

    public ResponseParser getResponseParser() {
        return responseParser;
    }

    public void setResponseParser(ResponseParser responseParser) {
        this.responseParser = responseParser;
    }

    public HeaderStore getHeaderStore() {
        return headerStore;
    }

    public void setHeaderStore(HeaderStore headerStore) {
        this.headerStore = headerStore;
    }
}
