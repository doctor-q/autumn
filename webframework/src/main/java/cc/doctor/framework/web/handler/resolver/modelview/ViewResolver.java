package cc.doctor.framework.web.handler.resolver.modelview;

import cc.doctor.framework.utils.CollectionUtils;
import cc.doctor.framework.utils.Container;
import cc.doctor.framework.web.handler.resolver.Resolver;
import cc.doctor.framework.web.route.GlobalConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Created by doctor on 2017/7/21.
 */
public abstract class ViewResolver implements Resolver {
    public static final Logger log = LoggerFactory.getLogger(ViewResolver.class);

    private GlobalConfig globalConfig = Container.container.getOrCreateComponent(GlobalConfig.class);

    public abstract String resolveView(ModelView modelView,
                                       HttpServlet servlet,
                                       HttpServletRequest servletRequest,
                                       HttpServletResponse servletResponse, Object data);

    public String getViewPath(ModelView modelView, HttpServlet servlet) {
        String rootPath = getWebPath(servlet);
        String prefix = getPrefix(modelView);
        String suffix = getSuffix(modelView);
        return CollectionUtils.join("/", rootPath, prefix, modelView.view() + "." + suffix);
    }

    public String getBaseHome(ModelView modelView, HttpServlet servlet) {
        String rootPath = getWebPath(servlet);
        String prefix = getPrefix(modelView);
        return rootPath + "/" + prefix;
    }

    public String getViewRelativePath(ModelView modelView) {
        String suffix = getSuffix(modelView);
        return modelView.view() + "." + suffix;
    }

    public String getWebPath(HttpServlet servlet) {
        ServletContext servletContext = servlet.getServletContext();
        return servletContext.getRealPath("/");
    }

    public String getPrefix(ModelView modelView) {
        String resolver = modelView.resolver();
        return globalConfig.getPrefix(resolver);
    }

    public String getSuffix(ModelView modelView) {
        String resolver = modelView.resolver();
        return globalConfig.getSuffix(resolver);
    }

}
