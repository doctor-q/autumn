package cc.doctor.framework.log.config;

import cc.doctor.framework.log.appender.Appender;
import cc.doctor.framework.log.appender.FileAppender;
import cc.doctor.framework.log.appender.RollingAppender;
import cc.doctor.framework.log.appender.encode.Encoder;
import cc.doctor.framework.log.logger.Logger;
import cc.doctor.framework.log.rolling.RollingPolicy;
import com.alibaba.fastjson.JSONObject;

import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;

public class JsonConfigReader implements ConfigReader {
    private static final String CLASS_FIELD = "class";
    private static final String LOG_CONFIG_JSON = "log.json";
    private Map<String, Appender> appenderMap = new HashMap<>();
    private Map<String, Logger> loggerMap = new HashMap<>();
    private Logger root;
    private JSONObject config;

    public Map<String, Appender> getAppenderMap() {
        return appenderMap;
    }

    public Map<String, Logger> getLoggerMap() {
        return loggerMap;
    }

    public Logger getRoot() {
        return root;
    }

    @Override
    public void load() {
        try {
            URL resource = JsonConfigReader.class.getClassLoader().getResource(LOG_CONFIG_JSON);
            byte[] bytes = Files.readAllBytes(Paths.get(resource.toURI()));
            config = JSONObject.parseObject(new String(bytes));
            loaderAppender();
            loaderRoot();
            loaderLoggers();
        } catch (IOException | URISyntaxException e) {

        }
    }

    public void loaderAppender() {
        JSONObject appenderObjectMap = config.getJSONObject("appender");
        for (Map.Entry<String, Object> entry : appenderObjectMap.entrySet()) {
            String appenderName = entry.getKey();
            JSONObject appenderObject = (JSONObject) entry.getValue();
            Appender appender = getAppender(appenderName, appenderObject);
            JSONObject encoderObject = appenderObject.getJSONObject("encoder");
            Encoder encoder = getEncoder(encoderObject);
            appender.setEncoder(encoder);
            if (appender instanceof RollingAppender) {
                JSONObject rollingPolicyObject = appenderObject.getJSONObject("rollingPolicy");
                RollingPolicy rollingPolicy = getRollingPolicy(rollingPolicyObject);
                ((RollingAppender) appender).setRollingPolicy(rollingPolicy);
            } else if (appender instanceof FileAppender) {
                String fileName = appenderObject.getString("fileName");
                ((FileAppender) appender).setFileName(fileName);
            }
            this.appenderMap.put(appenderName, appender);
        }
    }

    public void loaderRoot() {
        root = getLogger(Logger.ROOT_LOGGER_NAME, config.getJSONObject("root"), true);
    }

    public void loaderLoggers() {
        JSONObject loggerObject = config.getJSONObject("logger");
        for (Map.Entry<String, Object> entry : loggerObject.entrySet()) {
            String loggerName = entry.getKey();
            JSONObject value = (JSONObject) entry.getValue();
            loggerMap.put(loggerName, getLogger(loggerName, value, false));
        }
    }

    private Logger getLogger(String name, JSONObject loggerObject, boolean isRoot) {
        Logger logger = loggerObject.toJavaObject(Logger.class);
        logger.setName(name);
        if (!isRoot) {
            logger.setRoot(root);
        }
        for (String appName : logger.getAppenderRefs()) {
            Appender appender = appenderMap.get(appName);
            if (appender != null) {
                logger.addAppender(appender);
            }
        }
        return logger;
    }

    private Encoder getEncoder(JSONObject encoderObject) {
        String encoderClass = encoderObject.getString(CLASS_FIELD);
        try {
            Class<? extends Encoder> encode = (Class<? extends Encoder>) Class.forName(encoderClass);
            return encoderObject.toJavaObject(encode);
        } catch (Exception e) {
            return null;
        }
    }

    private RollingPolicy getRollingPolicy(JSONObject rollingPolicyObject) {
        String encoderClass = rollingPolicyObject.getString(CLASS_FIELD);
        try {
            Class<? extends RollingPolicy> rollingPolicy = (Class<? extends RollingPolicy>) Class.forName(encoderClass);
            return rollingPolicyObject.toJavaObject(rollingPolicy);
        } catch (Exception e) {
            return null;
        }
    }

    private Appender getAppender(String name, JSONObject appenderObject) {
        String appenderClass = appenderObject.getString(CLASS_FIELD);
        try {
            Class<? extends Appender> aClass = (Class<? extends Appender>) Class.forName(appenderClass);
            Appender appender = appenderObject.toJavaObject(aClass);
            appender.setName(name);
            return appender;
        } catch (Exception e) {
            throw new ConfigException();
        }
    }
}
