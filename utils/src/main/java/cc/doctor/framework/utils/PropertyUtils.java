package cc.doctor.framework.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.*;

/**
 * Created by doctor on 2017/3/8.
 */
public class PropertyUtils {
    public static final Logger log = LoggerFactory.getLogger(PropertyUtils.class);
    private static Map<String, Object> properties = new HashMap<>();

    static {
        String env = null;
        try {
            Process process = Runtime.getRuntime().exec("env");
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(process.getInputStream()));
            String line;
            while ((line = bufferedReader.readLine()) != null) {
                if (line.startsWith("project_env")) {
                    env = line.split("=") [1];
                    break;
                }
            }
        } catch (IOException e) {
            log.error("", e);
        }
        if (env == null) {
            env  = "local";
        }
        loadProperties(env + ".properties");
    }

    public static void loadProperties(String file) {
        Properties properties = new Properties();
        try {
            String sourceRoot = PropertyUtils.class.getResource("/").getFile();
            properties.load(new FileInputStream(sourceRoot + "/" + file));
            Enumeration<?> propertyNames = properties.propertyNames();
            while (propertyNames.hasMoreElements()) {
                String propertyName = propertyNames.nextElement().toString();
                PropertyUtils.properties.put(propertyName, properties.getProperty(propertyName));
            }
        } catch (IOException e) {
            log.error("load property file[{}] error.", file, e);
        }
    }

    public static <T> T getProperty(String key, T defaultValue) {
        T value = (T) properties.get(key);
        return value == null ? defaultValue : value;
    }

    public static void setProperty(String key, Object value) {
        properties.put(key, value);
    }

    public static String getString (String key, String defaultValue) {
        return properties.get(key) == null ? defaultValue : properties.get(key).toString();
    }

    public static Boolean getBoolean(String key, Boolean defaultValue) {
        return properties.get(key) == null ? defaultValue : Boolean.parseBoolean(properties.get(key).toString());
    }

    public static Integer getInteget(String key, Integer defaultValue) {
        return properties.get(key) == null ? defaultValue : Integer.parseInt(properties.get(key).toString());
    }

    public static Long getLong(String key, Long defaultValue) {
        return properties.get(key) == null ? defaultValue : Long.parseLong(properties.get(key).toString());
    }

    public static Float getFloat(String key, Float defaultValue) {
        return properties.get(key) == null ? defaultValue : Float.parseFloat(properties.get(key).toString());
    }

    public static Double getDouble(String key, Double defaultValue) {
        return properties.get(key) == null ? defaultValue : Double.parseDouble(properties.get(key).toString());
    }

    public static List<String> getList(String key, String separator) {
        return properties.get(key) == null ? new LinkedList<String>() : Arrays.asList(properties.get(key).toString().split(separator));
    }
}
