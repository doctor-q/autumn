package cc.doctor.framework.web.handler.resolver.modelview;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Created by doctor on 2017/7/21.
 */
public class ForwardViewResolver extends ViewResolver {

    @Override
    public String resolveView(ModelView modelView, HttpServlet servlet, HttpServletRequest servletRequest, HttpServletResponse servletResponse, Object data) {
        String prefix = getPrefix(modelView);
        String view = modelView.view();
        RequestDispatcher requestDispatcher = servlet.getServletContext().getRequestDispatcher("/" + prefix + "/" + view);
        try {
            requestDispatcher.forward(servletRequest, servletResponse);
        } catch (ServletException | IOException e) {
            throw new RuntimeException(e);
        }
        return null;
    }

    @Override
    public String getName() {
        return "forward";
    }

    @Override
    public String defaultSuffix() {
        return null;
    }
}
