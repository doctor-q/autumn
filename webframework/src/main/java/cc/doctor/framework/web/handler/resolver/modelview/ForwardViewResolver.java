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

    public ForwardViewResolver(ModelView modelView, HttpServlet servlet, HttpServletRequest servletRequest, HttpServletResponse servletResponse) {
        super(modelView, servlet, servletRequest, servletResponse);
    }

    @Override
    public String resolveView(ModelView modelView, Object data) {
        String prefix = getPrefix();
        String view = modelView.view();
        RequestDispatcher requestDispatcher = getServlet().getServletContext().getRequestDispatcher("/" + prefix + "/" + view);
        try {
            requestDispatcher.forward(getServletRequest(), getServletResponse());
        } catch (ServletException | IOException e) {
            throw new RuntimeException(e);
        }
        return null;
    }
}
