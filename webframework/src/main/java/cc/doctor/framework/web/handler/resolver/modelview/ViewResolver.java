package cc.doctor.framework.web.handler.resolver.modelview;

import cc.doctor.framework.web.Constants;
import cc.doctor.framework.web.handler.resolver.Resolver;
import cc.doctor.framework.web.route.RouteService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;

import static cc.doctor.framework.utils.Container.container;

/**
 * Created by doctor on 2017/7/21.
 */
public abstract class ViewResolver implements Resolver {
    public static final Logger log = LoggerFactory.getLogger(ViewResolver.class);
    private ModelView modelView;
    private HttpServlet servlet;
    private HttpServletRequest servletRequest;
    private HttpServletResponse servletResponse;
    private RouteService routeService = container.getOrCreateComponent(RouteService.class);

    public ViewResolver(ModelView modelView, HttpServlet servlet, HttpServletRequest servletRequest, HttpServletResponse servletResponse) {
        this.modelView = modelView;
        this.servlet = servlet;
        this.servletRequest = servletRequest;
        this.servletResponse = servletResponse;
    }

    public HttpServlet getServlet() {
        return servlet;
    }

    public HttpServletRequest getServletRequest() {
        return servletRequest;
    }

    public HttpServletResponse getServletResponse() {
        return servletResponse;
    }

    public ModelView getModelView() {
        return modelView;
    }

    @Override
    public String resolve(Object data) {
        return resolveView(modelView, data);
    }

    public abstract String resolveView(ModelView modelView, Object data);

    public String getViewBasedHome() {
        String rootPath = getWebPath();
        String prefix = getPrefix();
        if (prefix != null) {
            return rootPath + "/" + prefix;
        } else {
            return rootPath + "/";
        }
    }

    public String getWebPath() {
        ServletContext servletContext = servlet.getServletContext();
        return servletContext.getRealPath("/");
    }

    public String getPrefix() {
        String resolver = modelView.resolver();
        Map<String, String> viewResolver = routeService.getResolver(resolver);
        if (viewResolver != null) {
            return viewResolver.get("prefix");
        }
        return null;
    }
}
