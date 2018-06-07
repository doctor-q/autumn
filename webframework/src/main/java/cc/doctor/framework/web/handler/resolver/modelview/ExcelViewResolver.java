package cc.doctor.framework.web.handler.resolver.modelview;

import cc.doctor.framework.web.utils.POIUtils;
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


    @Override
    public String resolveView(ModelView modelView, HttpServlet servlet, HttpServletRequest servletRequest, HttpServletResponse servletResponse, Object data) {
        servletResponse.setHeader(Constants.CONTENT_TYPE, Constants.CONTENT_TYPE_EXCEL);
        Iterable iterable;
        if (! (data instanceof Iterable)) {
            iterable = Collections.singletonList(data);
        } else {
            iterable = (Iterable)data;
        }
        try {
            POIUtils.exportExcel(modelView.view(), null, iterable, servletResponse.getOutputStream());
        } catch (IOException e) {
            log.error("", e);
        }
        return null;
    }

    @Override
    public String getName() {
        return "excel";
    }
}
