package cc.doctor.framework.http.proxy;

import cc.doctor.framework.entity.Tuple;
import cc.doctor.framework.http.proxy.annotation.*;
import cc.doctor.framework.http.proxy.header.HeaderStore;
import cc.doctor.framework.http.proxy.response.ResponseParser;
import cc.doctor.framework.utils.HttpUtils;
import cc.doctor.framework.utils.ReflectUtils;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.*;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by doctor on 2017/11/24.
 * http service 动态代理
 */
public class HttpServiceProxy implements InvocationHandler {
    public static final Logger log = LoggerFactory.getLogger(HttpServiceProxy.class);
    private String baseUrl;
    private static Map<String, HttpServiceProxy> httpServiceProxyMap = new ConcurrentHashMap<>();
    private HeaderStore headerStore;
    private ResponseParser responseParser;

    public HttpServiceProxy(String baseUrl, ResponseParser responseParser) {
        this.baseUrl = baseUrl;
        this.responseParser = responseParser;
    }

    public static HttpServiceProxy get(String baseUrl, ResponseParser responseParser) {
        if (!httpServiceProxyMap.containsKey(baseUrl)) {
            httpServiceProxyMap.put(baseUrl, new HttpServiceProxy(baseUrl, responseParser));
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
        Class[] classes = {service};
        return (T) Proxy.newProxyInstance(HttpServiceProxy.class.getClassLoader(), classes, this);
    }

    public CloseableHttpResponse invoke(Method method, Object param) throws Exception {
        //todo http连接池，异步，缓存
        Tuple<Map<String, String>, Map<String, String>> getPostParamTuple;
        if (method.isAnnotationPresent(Get.class)) {
            Get get = method.getAnnotation(Get.class);
            log.info("[GET]{}{}", baseUrl, get.value());
            getPostParamTuple = paramToMap(param, true);
            Map<String, String> headers = null;
            if (method.isAnnotationPresent(Headers.class)) {
                headers = headerStore.getHeaders();
            }
            return HttpUtils.get(baseUrl + get.value(), getPostParamTuple.getT1(), headers);
        } else if (method.isAnnotationPresent(Post.class)) {
            Post post = method.getAnnotation(Post.class);
            log.info("[POST]{}{}", baseUrl, post.value());
            getPostParamTuple = paramToMap(param, false);
            Map<String, String> headers = null;
            if (method.isAnnotationPresent(Headers.class)) {
                headers = headerStore.getHeaders();
            }
            return HttpUtils.post(baseUrl + post.value(), getPostParamTuple.getT1(), getPostParamTuple.getT2(), headers);
        } else if (method.isAnnotationPresent(PostJson.class)) {
            PostJson postJson = method.getAnnotation(PostJson.class);
            log.info("[POST JSON]{}{}", baseUrl, postJson.value());
            getPostParamTuple = paramToMap(param, false);
            Map<String, String> headers = null;
            if (method.isAnnotationPresent(Headers.class)) {
                headers = headerStore.getHeaders();
            }

            return HttpUtils.postJson(baseUrl + postJson.value(), getPostParamTuple.getT1(), null, headers);
        } else if (method.isAnnotationPresent(MultiPart.class)) {
            MultiPart multiPart = method.getAnnotation(MultiPart.class);
            log.info("[Multipart]{}{}", baseUrl, multiPart.value());
            getPostParamTuple = paramToMap(param, false);
            String localFilePath = getPostParamTuple.getT2().get(multiPart.filePart());
            getPostParamTuple.getT2().remove(multiPart.filePart());
            return HttpUtils.multipart(baseUrl + multiPart.value(), localFilePath, multiPart.filePart(), getPostParamTuple.getT1(), getPostParamTuple.getT2(), null);
        } else {
            throw new Exception("Not http method");
        }
    }

    /**
     * convert param object to param map
     *
     * @param param a param instance
     * @param get   if method is get method
     * @return a tuple contains a get param map and a post param map
     */
    private Tuple<Map<String, String>, Map<String, String>> paramToMap(Object param, boolean get) {
        Map<String, String> getParamMap = new HashMap<>();
        Map<String, String> postParamMap = new HashMap<>();
        Map<String, Field> attrNameFields = ReflectUtils.getObjectAttrNameFields(param.getClass());
        for (String name : attrNameFields.keySet()) {
            Object obj = ReflectUtils.get(name, param);
            if (obj == null) {
                continue;
            }
            Field field = attrNameFields.get(name);

        }
        return new Tuple<>(getParamMap, postParamMap);
    }

    public void setBaseUrl(String baseUrl) {
        this.baseUrl = baseUrl;
    }
}
