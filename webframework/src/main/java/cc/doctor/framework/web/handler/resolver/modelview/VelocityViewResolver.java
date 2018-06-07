package cc.doctor.framework.web.handler.resolver.modelview;

import cc.doctor.framework.web.utils.VelocityUtils;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Created by doctor on 2017/7/21.
 */
public class VelocityViewResolver extends ViewResolver {


    @Override
    public String resolveView(ModelView modelView, HttpServlet servlet, HttpServletRequest servletRequest, HttpServletResponse servletResponse, Object data) {
        String viewBasedHome = getViewBasedHome(modelView, servlet);
        String view = modelView.view();
        String viewPath = viewBasedHome + "/" + view;
        return VelocityUtils.render(viewPath, data);
    }

    @Override
    public String getName() {
        return null;
    }
}
