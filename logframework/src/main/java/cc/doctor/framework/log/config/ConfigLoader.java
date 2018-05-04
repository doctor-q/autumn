package cc.doctor.framework.log.config;

import cc.doctor.framework.log.appender.Appender;
import cc.doctor.framework.log.appender.RollingAppender;
import cc.doctor.framework.log.appender.encode.Encoder;
import cc.doctor.framework.log.logger.Logger;
import cc.doctor.framework.log.rolling.RollingPolicy;
import com.alibaba.fastjson.JSONObject;

import java.lang.reflect.Constructor;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ConfigLoader {
    private JSONObject config;
    private Map<String, Appender> appenderMap = new HashMap<>();
    private Map<String, Logger> loggerMap = new HashMap<>();
    private Logger root;

    private static ConfigLoader INSTANCE;

    public static ConfigLoader getSingleton() {
        if (INSTANCE == null) {
            INSTANCE = new ConfigLoader();
        }
        return INSTANCE;
    }

    private ConfigLoader() {
    }

    public JSONObject getConfig() {
        return config;
    }

    public void setConfig(JSONObject config) {
        this.config = config;
    }

    public void loadAll() {
        loaderAppenders();
        loaderRoot();
        loaderLoggers();
    }

    public void loaderAppenders() {
        JSONObject appenderObjectMap = config.getJSONObject("appender");
        for (Map.Entry<String, Object> entry : appenderObjectMap.entrySet()) {
            String appenderName = entry.getKey();
            JSONObject value = (JSONObject) entry.getValue();
            String appenderClass = value.getString("class");
            Appender appender = construct(appenderClass);
            JSONObject encoderObject = value.getJSONObject("encoder");
            Encoder encoder = getEncoder(encoderObject);
            appender.setEncoder(encoder);
            if (appenderClass.equals(RollingAppender.class.getName())) {
                JSONObject rollingPolicyObject = value.getJSONObject("rollingPolicy");
                RollingPolicy rollingPolicy = getRollingPolicy(rollingPolicyObject);
                ((RollingAppender) appender).setRollingPolicy(rollingPolicy);
            }
            this.appenderMap.put(appenderName, appender);
        }
    }

    public void loaderRoot() {
        root = getLogger(config.getJSONObject("root"));
    }

    public void loaderLoggers() {
        JSONObject loggerObject = config.getJSONObject("logger");
        for (Map.Entry<String, Object> entry : loggerObject.entrySet()) {
            String loggerName = entry.getKey();
            JSONObject value = (JSONObject) entry.getValue();
            loggerMap.put(loggerName, getLogger(value));
        }
    }

    private Logger getLogger(JSONObject loggerObject) {
        Logger logger = new Logger();
        logger.setRoot(root);
        List appenderList = loggerObject.getObject("appender", List.class);
        for (Object appName : appenderList) {
            Appender appender = appenderMap.get(appName.toString());
            logger.addAppender(appender);
        }
        return logger;
    }

    private Encoder getEncoder(JSONObject encoderObject) {
        String encoderClass = encoderObject.getString("class");
        try {
            Class<? extends Encoder> encode = (Class<? extends Encoder>) Class.forName(encoderClass);
            return encoderObject.toJavaObject(encode);
        } catch (Exception e) {
            return null;
        }
    }

    private RollingPolicy getRollingPolicy(JSONObject rollingPolicyObject) {
        String encoderClass = rollingPolicyObject.getString("class");
        try {
            Class<? extends RollingPolicy> rollingPolicy = (Class<? extends RollingPolicy>) Class.forName(encoderClass);
            return rollingPolicyObject.toJavaObject(rollingPolicy);
        } catch (Exception e) {
            return null;
        }
    }

    private <T> T construct(String className) {
        try {
            Class<?> aClass = Class.forName(className);
            Constructor<T> constructor = (Constructor<T>) aClass.getConstructor();
            return constructor.newInstance();
        } catch (Exception e) {
            throw new ConfigException();
        }
    }
}
