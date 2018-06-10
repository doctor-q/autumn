package cc.doctor.framework.web.utils;

import freemarker.cache.StringTemplateLoader;
import freemarker.template.Configuration;
import freemarker.template.DefaultObjectWrapper;
import freemarker.template.Template;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.StringWriter;

/**
 * Created by doctor on 2017/7/30.
 */
public class FreeMarkerUtils {
    public static final Logger log = LoggerFactory.getLogger(FreeMarkerUtils.class);
    public static String renderTemplate(String base, String path, Object data) {
        Configuration configuration = new Configuration(Configuration.DEFAULT_INCOMPATIBLE_IMPROVEMENTS);
        try {
            configuration.setTemplateLoader(new StringTemplateLoader());
            configuration.setDirectoryForTemplateLoading(new File(base));
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
