package cc.doctor.framework.web.handler;

import cc.doctor.framework.utils.ReflectUtils;
import cc.doctor.framework.web.Constants;
import cc.doctor.framework.web.handler.invoke.Invoker;
import cc.doctor.framework.web.handler.invoke.Parameter;
import cc.doctor.framework.web.route.RouteInvoke;
import cc.doctor.framework.web.servlet.meta.HttpMetadata;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.ServletInputStream;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.*;

/**
 * Created by doctor on 2017/5/21.
 */
public class RequestParser {
    private static final Logger log = LoggerFactory.getLogger(ReflectUtils.class);

    public Object invoke(HttpServlet servlet, HttpServletRequest servletRequest, HttpServletResponse servletResponse, RouteInvoke routeInvoke) throws Throwable {
        HttpMetadata httpMetadata = getHttpMetadata(servletRequest);
        try {
            Invoker invoker = routeInvoke.getAndBuildInvoker();
            return invoker.invoke(httpMetadata);
        } catch (Throwable e) {
            log.error("", e);
            throw e;
        }
    }

    private HttpMetadata getHttpMetadata(HttpServletRequest servletRequest) {
        HttpMetadata httpMetadata = new HttpMetadata();

        httpMetadata.setPath(servletRequest.getPathInfo());
        httpMetadata.setMethod(servletRequest.getMethod());
        Cookie[] cookies = servletRequest.getCookies();
        if (cookies != null) {
            httpMetadata.setCookies(Arrays.asList(cookies));
        }
        Enumeration headerNames = servletRequest.getHeaderNames();
        Map<String, String> headers = new HashMap<>();
        while (headerNames.hasMoreElements()) {
            Object element = headerNames.nextElement();
            String header = servletRequest.getHeader(element.toString());
            headers.put(element.toString(), header);
        }
        httpMetadata.setHeaders(headers);

        String contentType = servletRequest.getHeader(Constants.CONTENT_TYPE);
        if (contentType != null && contentType.equals(Constants.CONTENT_TYPE_JSON)) {
            try {
                ServletInputStream inputStream = servletRequest.getInputStream();
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
                String line = null;
                StringBuilder jsonBuilder = new StringBuilder();
                while ((line = bufferedReader.readLine()) != null) {
                    jsonBuilder.append(line);
                }
                httpMetadata.setJson(jsonBuilder.toString());
            } catch (IOException e) {
                log.error("", e);
            }
        } else {
            Map<String, String> parameters = new HashMap<>();
            Map parameterMap = servletRequest.getParameterMap();
            for (Object key : parameterMap.keySet()) {
                Object param = parameterMap.get(key);
                if (param != null) {
                    if (((String []) param).length > 0) {
                        parameters.put(key.toString(), ((String[]) param)[0]);
                    }
                }
            }

            httpMetadata.setParams(parameters);
        }
        Map<String, Object> attributes = new HashMap<>();
        Enumeration attributeNames = servletRequest.getAttributeNames();
        while (attributeNames.hasMoreElements()) {
            Object element = attributeNames.nextElement();
            attributes.put(element.toString(), servletRequest.getAttribute(element.toString()));
        }
        httpMetadata.setAttributes(attributes);
        return httpMetadata;
    }
}
