package cc.doctor.framework.web.handler.resolver.modelview;

import cc.doctor.framework.utils.POIUtils;
import cc.doctor.framework.web.Constants;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Collections;

/**
 * Created by doctor on 2017/7/30.
 */
public class ExcelViewResolver extends ViewResolver {
    public ExcelViewResolver(ModelView modelView, HttpServlet servlet, HttpServletRequest servletRequest, HttpServletResponse servletResponse) {
        super(modelView, servlet, servletRequest, servletResponse);
    }

    @Override
    public String resolveView(ModelView modelView, Object data) {
        getServletResponse().setHeader(Constants.CONTENT_TYPE, Constants.CONTENT_TYPE_EXCEL);
        Iterable iterable;
        if (! (data instanceof Iterable)) {
            iterable = Collections.singletonList(data);
        } else {
            iterable = (Iterable)data;
        }
        try {
            POIUtils.exportExcel(modelView.view(), null, iterable, getServletResponse().getOutputStream());
        } catch (IOException e) {
            log.error("", e);
        }
        return null;
    }
}
