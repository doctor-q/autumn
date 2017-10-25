package cc.doctor.framework.utils;

import freemarker.core.ParseException;
import freemarker.template.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;

/**
 * Created by doctor on 2017/7/30.
 */
public class FreeMarkerUtils {
    public static final Logger log = LoggerFactory.getLogger(FreeMarkerUtils.class);
    public static String renderTemplate(String root, String path, Object data) {
        Configuration configuration = new Configuration(Configuration.DEFAULT_INCOMPATIBLE_IMPROVEMENTS);
        try {
            configuration.setDirectoryForTemplateLoading(new File(root));
            configuration.setObjectWrapper(new DefaultObjectWrapper(Configuration.DEFAULT_INCOMPATIBLE_IMPROVEMENTS));
            configuration.setDefaultEncoding("UTF-8");   //这个一定要设置，不然在生成的页面中 会乱码
            Template template = configuration.getTemplate(path);
            StringWriter writer = new StringWriter();
            template.process(data, writer);
            return writer.toString();
        } catch (Exception e) {
            log.error("", e);
        }
        return null;
    }
}
