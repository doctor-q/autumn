package cc.doctor.framework.web.handler.resolver.modelview;

import cc.doctor.framework.web.handler.resolver.Resolver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;

/**
 * Created by doctor on 2017/7/21.
 */
public abstract class ViewResolver implements Resolver {
    public static final Logger log = LoggerFactory.getLogger(ViewResolver.class);

    public abstract String resolveView(ModelView modelView,
            HttpServlet servlet,
            HttpServletRequest servletRequest,
            HttpServletResponse servletResponse, Object data);

    public String getViewBasedHome(ModelView modelView, HttpServlet servlet) {
        String rootPath = getWebPath(servlet);
        String prefix = getPrefix(modelView);
        if (prefix != null) {
            return rootPath + "/" + prefix;
        } else {
            return rootPath + "/";
        }
    }

    public String getWebPath(HttpServlet servlet) {
        ServletContext servletContext = servlet.getServletContext();
        return servletContext.getRealPath("/");
    }

    public String getPrefix(ModelView modelView) {
        String resolver = modelView.resolver();
        Map<String, String> viewResolver = null;
        if (viewResolver != null) {
            return viewResolver.get("prefix");
        }
        return null;
    }

}
