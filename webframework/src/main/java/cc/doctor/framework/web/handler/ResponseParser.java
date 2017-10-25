package cc.doctor.framework.web.handler;

import cc.doctor.framework.utils.ReflectUtils;
import cc.doctor.framework.utils.SerializeUtils;
import cc.doctor.framework.web.Constants;
import cc.doctor.framework.web.handler.out.ResponseAnnotationHandler;
import cc.doctor.framework.web.handler.out.ResponseHandlerFactory;
import cc.doctor.framework.web.handler.resolver.Resolver;
import cc.doctor.framework.web.handler.resolver.json.JsonBody;
import cc.doctor.framework.web.handler.resolver.json.JsonResolver;
import cc.doctor.framework.web.handler.resolver.modelview.ModelView;
import cc.doctor.framework.web.handler.resolver.modelview.ViewResolverFactory;
import cc.doctor.framework.web.route.RouteInvoke;
import cc.doctor.framework.web.servlet.DispatchServlet;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * Created by doctor on 2017/6/29.
 */
public class ResponseParser {
    public static final ResponseParser responseParser = new ResponseParser();
    private List<Class> parseClasses = new LinkedList<>();
    public static final Logger log = LoggerFactory.getLogger(ResponseParser.class);

    private ResponseParser() {
    }

    public void addParseClasses(Class clazz) {
        parseClasses.add(clazz);
    }

    public void invoke(Object data, RouteInvoke routeInvoke, HttpServlet servlet, HttpServletRequest servletRequest, HttpServletResponse servletResponse) throws IOException {
        Object ret = doParse(data);
        // do resolver
        Resolver resolver = new JsonResolver();
        Method method = routeInvoke.getMethod();
        Annotation[] annotations = method.getAnnotations();
        for (Annotation annotation : annotations) {
            if (annotation.annotationType().equals(JsonBody.class)) {
                resolver = new JsonResolver();
                servletResponse.setHeader(Constants.CONTENT_TYPE, Constants.CONTENT_TYPE_JSON);
                break;
            }
            if (annotation.annotationType().equals(ModelView.class)) {
                servletResponse.setHeader(Constants.CONTENT_TYPE, Constants.CONTENT_TYPE_HTML);
                ModelView modelView = (ModelView) annotation;
                resolver = new ViewResolverFactory(modelView, servlet, servletRequest, servletResponse);
            }
        }
        try {
            String resolve = resolver.resolve(ret);
            if (resolve != null) {
                PrintWriter writer = servletResponse.getWriter();
                writer.write(resolve);
            }
        } catch (Exception e) {
            log.error("", e);
            throw e;
        }
    }

    public Object doParse(Object data) {
        Object ret = data;
        if (parseClasses != null) {
            if (data instanceof Iterable) {
                List<Object> list = new LinkedList<>();
                Iterable iterable = (Iterable) data;
                for (Object obj : iterable) {
                    for (Class parseClass : parseClasses) {
                        if (parseClass.isAssignableFrom(obj.getClass())) {
                            obj = parseObject(obj);
                            break;
                        }
                    }
                    list.add(obj);
                }
                ret = list;
            } else {
                for (Class parseClass : parseClasses) {
                    if (parseClass.isAssignableFrom(data.getClass())) {
                        ret = parseObject(data);
                        break;
                    }
                }
            }
        }
        return ret;
    }

    private Map<String, Object> parseObject(Object obj) {
        Map<String, Object> map = new LinkedHashMap<>();
        Map<String, Field> attrNameTypes = ReflectUtils.getObjectAttrNameFields(obj.getClass());
        for (String attrName : attrNameTypes.keySet()) {
            Field field = attrNameTypes.get(attrName);
            Object value = ReflectUtils.get(field.getName(), obj);
            map.put(field.getName(), value);
            Annotation[] annotations = field.getAnnotations();
            for (Annotation annotation : annotations) {
                ResponseAnnotationHandler annotationHandler = ResponseHandlerFactory.getAnnotationHandler(annotation.annotationType());
                if (annotationHandler == null) {
                    continue;
                }
                Object after = annotationHandler.handler(value, annotation);
                map.put(field.getName(), after);
            }
        }
        return map;
    }
}
