package cc.doctor.framework.web.route;

import cc.doctor.framework.entity.Tuple;
import cc.doctor.framework.utils.Container;
import cc.doctor.framework.utils.PropertyUtils;
import cc.doctor.framework.web.exception.SimpleResultExceptionHandler;
import cc.doctor.framework.web.handler.resolver.Resolver;
import cc.doctor.framework.web.handler.resolver.ResolverRegistry;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class GlobalConfig {
    private String globalExceptionHandler;
    private String globalModelViewResolver;
    // 视图处理器配置
    private Map<String, Tuple<String, String>> resolverConfMap;
    private String controllerScanPackage;

    public GlobalConfig() {
        globalExceptionHandler = PropertyUtils.getString("mvc.global.exceptionhandler", SimpleResultExceptionHandler.class.getName());
        globalModelViewResolver = PropertyUtils.getString("mvc.modelview.global.resolver", null);
        controllerScanPackage = PropertyUtils.getString("mvc.global.controller.scan", null);
    }

    public String getGlobalExceptionHandler() {
        return globalExceptionHandler;
    }

    public void setGlobalExceptionHandler(String globalExceptionHandler) {
        this.globalExceptionHandler = globalExceptionHandler;
    }

    public String getGlobalModelViewResolver() {
        return globalModelViewResolver;
    }

    public void setGlobalModelViewResolver(String globalModelViewResolver) {
        this.globalModelViewResolver = globalModelViewResolver;
    }

    public String getControllerScanPackage() {
        return controllerScanPackage;
    }

    public void setControllerScanPackage(String controllerScanPackage) {
        this.controllerScanPackage = controllerScanPackage;
    }

    public String getPrefix(String resolver) {

        Tuple<String, String> tuple = getResolverConfig(resolver);
        return tuple.getT1();
    }

    public String getSuffix(String resolver) {
        Tuple<String, String> tuple = getResolverConfig(resolver);
        return tuple.getT2();
    }

    private synchronized Tuple<String, String> getResolverConfig(String resolverName) {
        if (resolverConfMap == null) {
            resolverConfMap = new HashMap<>();
            ResolverRegistry resolverRegistry = Container.container.getOrCreateComponent(ResolverRegistry.class);
            Collection<Resolver> resolvers = resolverRegistry.getResolvers();
            for (Resolver resolver : resolvers) {
                String suffix = PropertyUtils.getString("mvc.modelview." + resolver.getName() + ".suffix", null);
                suffix = suffix == null ? resolver.defaultSuffix() : suffix;
                String prefix = PropertyUtils.getString("mvc.modelview." + resolver.getName() + ".prefix", null);
                prefix = prefix == null ? "" : prefix;
                Tuple<String, String> tuple = new Tuple<>(prefix, suffix);
                resolverConfMap.put(resolver.getName(), tuple);
            }
        }
        return resolverConfMap.get(resolverName);
    }
}
