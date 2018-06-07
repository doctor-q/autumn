package cc.doctor.framework.web.handler.resolver;

import cc.doctor.framework.utils.Container;
import cc.doctor.framework.web.handler.resolver.modelview.ExcelViewResolver;
import cc.doctor.framework.web.handler.resolver.modelview.ForwardViewResolver;
import cc.doctor.framework.web.handler.resolver.modelview.FreemarkerViewResolver;
import cc.doctor.framework.web.handler.resolver.modelview.VelocityViewResolver;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class ResolverRegistry {
    private Map<String, Resolver> resolverMap = new ConcurrentHashMap<>();
    private Map<Class<? extends Resolver>, Resolver> classResolverMap = new ConcurrentHashMap<>();

    public ResolverRegistry() {
        register(ExcelViewResolver.class);
        register(ForwardViewResolver.class);
        register(FreemarkerViewResolver.class);
        register(VelocityViewResolver.class);
    }

    public void register(Class<? extends Resolver> resolverClass) {
        Resolver resolver = Container.container.getOrCreateComponent(resolverClass);
        classResolverMap.put(resolverClass, resolver);
        String name = resolver.getName();
        resolverMap.put(name, resolver);
    }

    public <T extends Resolver> T getResolver(Class<T> resolverClass) {
        return (T) classResolverMap.get(resolverClass);
    }

    public Resolver getResolver(String name) {
        return resolverMap.get(name);
    }
}
