package cc.doctor.framework.web.handler.resolver.modelview;

import cc.doctor.framework.web.route.RouteService;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.lang.reflect.Constructor;
import java.util.List;
import java.util.Map;

import static cc.doctor.framework.utils.Container.container;

/**
 * Created by doctor on 2017/7/21.
 */
public class ViewResolverFactory extends ViewResolver {
    private RouteService routeService = container.getOrCreateComponent(RouteService.class);

    public ViewResolverFactory(ModelView modelView, HttpServlet servlet, HttpServletRequest servletRequest, HttpServletResponse servletResponse) {
        super(modelView, servlet, servletRequest, servletResponse);
    }

    @Override
    public String resolveView(ModelView modelView, Object data) {
        ViewResolver viewResolver = createViewResolver();
        if (viewResolver == null) {
            throw new RuntimeException("Get resolver error.");
        }
        return viewResolver.resolveView(modelView, data);
    }

    public ViewResolver createViewResolver() {
        ModelView modelView = getModelView();
        String resolver = modelView.resolver();
        Map<String, String> viewResolver = routeService.getResolver(resolver);
        String resolverClass = viewResolver.get("resolverClass");
        if (resolverClass == null) {
            resolverClass = ForwardViewResolver.class.getName();
        }
        try {
            Class<?> resolverClazz = Class.forName(resolverClass);
            Constructor<?> constructor = resolverClazz.getConstructor(ModelView.class, HttpServlet.class, HttpServletRequest.class, HttpServletResponse.class);
            Object instance = constructor.newInstance(modelView, getServlet(), getServletRequest(), getServletResponse());
            if (instance instanceof ViewResolver) {
                return (ViewResolver) instance;
            }
        } catch (Exception e) {
            log.error("", e);
            return null;
        }
        return null;
    }
}
