package cc.doctor.framework.web.handler.resolver.modelview;

import cc.doctor.framework.utils.FreeMarkerUtils;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Created by doctor on 2017/7/30.
 */
public class FreemarkerViewResolver extends ViewResolver {
    public FreemarkerViewResolver(ModelView modelView, HttpServlet servlet, HttpServletRequest servletRequest, HttpServletResponse servletResponse) {
        super(modelView, servlet, servletRequest, servletResponse);
    }

    @Override
    public String resolveView(ModelView modelView, Object data) {
        return FreeMarkerUtils.renderTemplate(getViewBasedHome(), modelView.view(), data);
    }
}
