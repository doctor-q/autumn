package cc.doctor.framework.web.route;

import cc.doctor.framework.utils.FileUtils;
import cc.doctor.framework.utils.ReflectUtils;
import cc.doctor.framework.utils.SerializeUtils;
import cc.doctor.framework.web.handler.ResponseParser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Method;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

/**
 * Created by doctor on 2017/7/17.
 */
public class RouteService {
    public static final Logger log = LoggerFactory.getLogger(RouteService.class);
    private RoutesConfig routesConfig;

    public RouteService() {
        loadRoutes();
    }

    public RoutesConfig getRoutesConfig() {
        return routesConfig;
    }

    public void loadRoutes() {
        String json = FileUtils.readFile(RouteService.class.getResource("/").getPath() + "/" + "routes.json");
        routesConfig = SerializeUtils.jsonToObject(json, RoutesConfig.class);
        //set resolver
        setRequestParser(routesConfig.get__requestParser());
        setResponseParser(routesConfig.get__responseParser());
        // sort routes
        TreeMap<String, Object> treeMap = new TreeMap<>();
        copyMap(routesConfig.get__routes(), treeMap);
        routesConfig.set__routes(treeMap);
    }

    // todo merge routes
    public void mergeRoutes(String routesFile) {

    }

    public RouteInvoke getInvoke(String path) {
        List<String> serviceMethod = findServiceMethod(routesConfig.get__routes(), path);
        RouteInvoke routeInvoke = new RouteInvoke();
        if (serviceMethod == null) {
            routeInvoke.setFound(false);
            return routeInvoke;
        }
        routeInvoke.setFound(true);
        if (serviceMethod.size() == 2) {
            routeInvoke.setService(serviceMethod.get(0));
            routeInvoke.setMethodName(serviceMethod.get(1));
        } else if (serviceMethod.size() == 3) {
            routeInvoke.setService(serviceMethod.get(0));
            routeInvoke.setMethodName(serviceMethod.get(1));
            routeInvoke.setExceptionHandler(serviceMethod.get(2));
        }
        Class serviceClass = null;
        try {
            serviceClass = Class.forName(routeInvoke.getService());
        } catch (ClassNotFoundException e) {
            try {
                if (routesConfig.get__default_service_package() != null) {
                    serviceClass = Class.forName(routesConfig.get__default_service_package() + "." + routeInvoke.getService());
                }
            } catch (ClassNotFoundException e1) {
                log.error("", e1);
            }
        }
        if (serviceClass != null) {
            Method methodByName = ReflectUtils.getMethodByName(routeInvoke.getMethodName(), serviceClass);
            routeInvoke.setMethod(methodByName);
        }
        routeInvoke.setServiceClass(serviceClass);
        return routeInvoke;
    }

    private List<String> findServiceMethod(Map routes, String fullPath) {
        for (Object path : routes.keySet()) {
            if (fullPath.startsWith(path.toString())) {
                Object subRoutes = routes.get(path);
                if (subRoutes instanceof List) {
                    return (List<String>) subRoutes;
                } else {
                    return findServiceMethod((Map) subRoutes, fullPath.substring(path.toString().length()));
                }
            } else if (path.toString().equals("__other")) { //other置于最后
                return (List<String>) routes.get(path);
            }
        }
        return null;
    }

    private void copyMap(Map src, Map dest) {
        for (Object key : src.keySet()) {
            Object value = src.get(key);
            if (value instanceof Map) {
                Map child = new TreeMap<>();
                copyMap((Map) value, child);
                dest.put(key, child);
            } else {
                dest.put(key, value);
            }
        }
    }

    public void setRequestParser(Map<String,Object> requestParser) {
    }

    public void setResponseParser(Map<String,Object> responseParser) {
        if (responseParser != null) {
            Object parseClasses = responseParser.get("parseClasses");
            if (parseClasses != null && parseClasses instanceof List) {
                for (Object parseClass : (List) parseClasses) {
                    try {
                        Class<?> aClass = Class.forName(parseClass.toString());
                        ResponseParser.responseParser.addParseClasses(aClass);
                    } catch (ClassNotFoundException e) {
                        log.error("", e);
                    }
                }
            }
        }
    }

    public Map<String, String> getResolver(String resolverName) {
        List<Map<String, String>> viewResolvers = routesConfig.get__viewResolvers();
        for (Map<String, String> viewResolver : viewResolvers) {
            String name = viewResolver.get("name");
            String resolverClass = viewResolver.get("resolverClass");
            if ((name != null && name.equals(resolverName)) ||
                    (resolverClass != null && resolverClass.equals(resolverName))) {
                return viewResolver;
            }
        }
        return null;
    }
}
