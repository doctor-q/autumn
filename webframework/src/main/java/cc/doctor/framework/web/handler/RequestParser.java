package cc.doctor.framework.web.handler;

import cc.doctor.framework.utils.ReflectUtils;
import cc.doctor.framework.entity.Tuple;
import cc.doctor.framework.web.Constants;
import cc.doctor.framework.web.handler.parser.ParameterType;
import cc.doctor.framework.web.route.RouteInvoke;
import cc.doctor.framework.web.servlet.meta.HttpMetaData;
import cc.doctor.framework.web.servlet.meta.HttpMetaParameters;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.ServletInputStream;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.annotation.Annotation;
import java.util.*;

import static cc.doctor.framework.utils.Container.container;

/**
 * Created by doctor on 2017/5/21.
 */
public class RequestParser {
    private static final Logger log = LoggerFactory.getLogger(ReflectUtils.class);
    public static final RequestParser requestParser = new RequestParser();

    private HttpMetaData preService(HttpServletRequest request) {
        HttpMetaData httpMetaData = new HttpMetaData();
        httpMetaData.setPath(request.getPathInfo());
        httpMetaData.setMethod(request.getMethod());
        httpMetaData.setCookies(request.getCookies());
        Enumeration headerNames = request.getHeaderNames();
        Map<String, String> headers = new HashMap<>();
        while (headerNames.hasMoreElements()) {
            Object element = headerNames.nextElement();
            String header = request.getHeader(element.toString());
            headers.put(element.toString(), header);
        }
        httpMetaData.setHeaders(headers);
        return httpMetaData;
    }

    public Object invoke(HttpServlet servlet, HttpServletRequest servletRequest, HttpServletResponse servletResponse, RouteInvoke routeInvoke) throws Exception {
        HttpMetaData httpMetaData = preService(servletRequest);
        HttpMetaParameters httpMetaParameters = preParseParameters(servletRequest);
        try {
            Object serviceInstance = container.getOrCreateComponent(routeInvoke.getServiceClass());
            if (routeInvoke.getMethod() == null) {
                log.error("Method not found [{}]", routeInvoke.getMethodName());
                throw new RuntimeException("No such method.");
            }
            List<Tuple<Class, Annotation[]>> paramTypeAnnotations = ReflectUtils.getMethodParamTypes(routeInvoke.getMethod());
            List<Object> params = new LinkedList<>();
            for (Tuple<Class, Annotation[]> paramTypeAnnotation : paramTypeAnnotations) {
                ParameterType parameterType = ParameterType.get(paramTypeAnnotation.getT1(), paramTypeAnnotation.getT2());
                if (parameterType == null) {
                    throw new RuntimeException("Parameter should declared.");
                }
                Object instance = parameterType.transfer(paramTypeAnnotation.getT1(), paramTypeAnnotation.getT2(), httpMetaData, httpMetaParameters);
                params.add(instance);
            }
            return routeInvoke.getMethod().invoke(serviceInstance, params.toArray());
        } catch (Exception e) {
            log.error("", e);
            throw e;
        }
    }

    private HttpMetaParameters preParseParameters(HttpServletRequest servletRequest) {
        HttpMetaParameters httpMetaParameters = new HttpMetaParameters();
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
                httpMetaParameters.setJson(jsonBuilder.toString());
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

            httpMetaParameters.setParams(parameters);
        }
        Map<String, Object> attributes = new HashMap<>();
        Enumeration attributeNames = servletRequest.getAttributeNames();
        while (attributeNames.hasMoreElements()) {
            Object element = attributeNames.nextElement();
            attributes.put(element.toString(), servletRequest.getAttribute(element.toString()));
        }
        httpMetaParameters.setAttributes(attributes);
        return httpMetaParameters;
    }
}
