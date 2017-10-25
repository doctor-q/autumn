package cc.doctor.framework.web.route;

import java.util.List;
import java.util.Map;
import java.util.TreeMap;

/**
 * Created by doctor on 2017/7/17.
 */
public class RoutesConfig {
    // 异常处理器定义
    private Map<String, String> __exceptionHandlers;
    // 默认异常处理器，当接口没有特殊异常处理器时使用默认异常处理器
    private String __globalExceptionHandler;
    // 请求响应处理
    private Map<String, Object> __requestParser;
    private Map<String, Object> __responseParser;
    // 视图解析器
    private List<Map<String, String>> __viewResolvers;
    // 默认服务包，当没有配置全路径时使用这个包下面的服务
    private String __default_service_package;
    // 路由映射，路由之间的层级关系
    private Map<String, Object> __routes;

    public Map<String, String> get__exceptionHandlers() {
        return __exceptionHandlers;
    }

    public void set__exceptionHandlers(Map<String, String> __exceptionHandlers) {
        this.__exceptionHandlers = __exceptionHandlers;
    }

    public String get__globalExceptionHandler() {
        return __globalExceptionHandler;
    }

    public void set__globalExceptionHandler(String __globalExceptionHandler) {
        this.__globalExceptionHandler = __globalExceptionHandler;
    }

    public Map<String, Object> get__requestParser() {
        return __requestParser;
    }

    public void set__requestParser(Map<String, Object> __requestParser) {
        this.__requestParser = __requestParser;
    }

    public Map<String, Object> get__responseParser() {
        return __responseParser;
    }

    public void set__responseParser(Map<String, Object> __responseParser) {
        this.__responseParser = __responseParser;
    }

    public List<Map<String, String>> get__viewResolvers() {
        return __viewResolvers;
    }

    public void set__viewResolvers(List<Map<String, String>> __viewResolvers) {
        this.__viewResolvers = __viewResolvers;
    }

    public String get__default_service_package() {
        return __default_service_package;
    }

    public void set__default_service_package(String __default_service_package) {
        this.__default_service_package = __default_service_package;
    }

    public Map<String, Object> get__routes() {
        return __routes;
    }

    public void set__routes(Map<String, Object> __routes) {
        this.__routes = __routes;
    }
}
