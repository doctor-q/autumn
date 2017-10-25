package cc.doctor.framework.web.handler.resolver.modelview;

import cc.doctor.framework.utils.VelocityUtils;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Created by doctor on 2017/7/21.
 */
public class VelocityViewResolver extends ViewResolver {
    public VelocityViewResolver(ModelView modelView, HttpServlet servlet, HttpServletRequest servletRequest, HttpServletResponse servletResponse) {
        super(modelView, servlet, servletRequest, servletResponse);
    }

    @Override
    public String resolveView(ModelView modelView, Object data) {
        String viewBasedHome = getViewBasedHome();
        String view = modelView.view();
        String viewPath = viewBasedHome + "/" + view;
        return VelocityUtils.render(viewPath, data);
    }
}
