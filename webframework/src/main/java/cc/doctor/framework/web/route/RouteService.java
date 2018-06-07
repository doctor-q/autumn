package cc.doctor.framework.web.route;

import cc.doctor.framework.utils.Container;
import cc.doctor.framework.utils.ReflectUtils;
import cc.doctor.framework.web.handler.ResponseParser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Method;
import java.util.*;

/**
 * Created by doctor on 2017/7/17.
 */
public class RouteService {
    public static final Logger log = LoggerFactory.getLogger(RouteService.class);

    private GlobalConfig globalConfig = Container.container.getOrCreateComponent(GlobalConfig.class);
    private PackageScan packageScan = Container.container.getOrCreateComponent(PackageScan.class);
    private List<Class> controllers;
    private Map<String, RouteInvoke> routeInvokes;

    public RouteInvoke getInvoke(String path) {
        if (controllers == null) {
            controllers = packageScan.scanPackage(globalConfig.getControllerScanPackage());
        }
        if (routeInvokes == null) {
            routeInvokes = new TreeMap<>();
            scanControllers();
        }
        return routeInvokes.get(path);
    }


    public void scanControllers() {
        for (Class controller : controllers) {
            String base = "";
            if (controller.isAnnotationPresent(RequestMapping.class)) {
                RequestMapping requestMapping = (RequestMapping) controller.getAnnotation(RequestMapping.class);
                base = requestMapping.value();
            }
            Method[] methods = controller.getMethods();
            for (Method method : methods) {
                if (method.isAnnotationPresent(RequestMapping.class)) {
                    RouteInvoke routeInvoke = new RouteInvoke();

                    RequestMapping requestMapping = method.getAnnotation(RequestMapping.class);
                    String value = requestMapping.value();
                    String url = base + value;
                    routeInvoke.setPath(url);
                    routeInvoke.setControllerClass(controller);
                    routeInvoke.setMethod(method);
                    routeInvokes.put(url, routeInvoke);
                }
            }
        }

    }
}
