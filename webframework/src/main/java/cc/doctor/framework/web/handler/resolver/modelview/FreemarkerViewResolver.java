package cc.doctor.framework.web.handler.resolver.modelview;

import cc.doctor.framework.web.utils.FreeMarkerUtils;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Created by doctor on 2017/7/30.
 */
public class FreemarkerViewResolver extends ViewResolver {


    @Override
    public String resolveView(ModelView modelView, HttpServlet servlet, HttpServletRequest servletRequest, HttpServletResponse servletResponse, Object data) {
        return FreeMarkerUtils.renderTemplate(getViewBasedHome(modelView, servlet), modelView.view(), data);
    }

    @Override
    public String getName() {
        return "freemarker";
    }
}
