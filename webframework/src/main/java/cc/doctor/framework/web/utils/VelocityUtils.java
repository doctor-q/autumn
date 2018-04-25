package cc.doctor.framework.web.utils;

import cc.doctor.framework.utils.ReflectUtils;
import org.apache.velocity.Template;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.VelocityEngine;

import java.io.StringWriter;
import java.lang.reflect.Field;
import java.util.*;

/**
 * Created by doctor on 2017/7/23.
 */
public class VelocityUtils {
    private static VelocityEngine velocityEngine = new VelocityEngine();
    static {
        Properties properties = new Properties();
        properties.setProperty("resource.loader", "file");
        properties.setProperty("file.resource.loader.class", "org.apache.velocity.runtime.resource.loader.FileResourceLoader");
        properties.setProperty("file.resource.loader.path", "/");
        velocityEngine.init(properties);
    }

    public static String render(Template template, Object data) {
        VelocityContext velocityContext = new VelocityContext();
        StringWriter stringWriter = new StringWriter();
        if (data instanceof Map) {
            Map map = (Map) data;
            for (Object key : map.keySet()) {
                velocityContext.put(key.toString(), map.get(key));
            }
        } else if (data instanceof Iterable) {
            velocityContext.put("list", data);
        } else {
            Map<String, Field> attrNameFields = ReflectUtils.getObjectAttrNameFields(data.getClass());
            if (attrNameFields != null) {
                for (String attr : attrNameFields.keySet()) {
                    Object value = ReflectUtils.get(attr, data);
                    if (value != null) {
                        velocityContext.put(attr, value);
                    }
                }
            }
        }
        template.merge(velocityContext, stringWriter);
        return stringWriter.toString();
    }

    // path is absolutely
    public static String render(String path, Object data) {
        Template template = velocityEngine.getTemplate(path);
        return render(template, data);
    }
}
