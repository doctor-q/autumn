package cc.doctor.framework.log;

import cc.doctor.framework.log.config.ConfigReader;
import cc.doctor.framework.log.config.JsonConfigReader;
import org.slf4j.ILoggerFactory;
import org.slf4j.Logger;

public class LoggerFactory implements ILoggerFactory {
    private static LoggerFactory loggerFactory;
    private static final String LOGGER_ROOT = "ROOT";
    private ConfigReader configReader = new JsonConfigReader();

    public static LoggerFactory getSingleton() {
        if (loggerFactory == null) {
            loggerFactory = new LoggerFactory();
            loggerFactory.init();
        }
        return loggerFactory;
    }

    public void init() {
        configReader.load();
    }

    @Override
    public Logger getLogger(String name) {
        if (name.equalsIgnoreCase(LOGGER_ROOT)) {
            return configReader.getRoot();
        }
        if (configReader.getLoggerMap().get(name) != null) {
            return configReader.getLoggerMap().get(name);
        }
        String nameCopy = name;
        while (true) {
            int index = nameCopy.lastIndexOf('.');
            if (index == -1) {
                break;
            }
            nameCopy = nameCopy.substring(0, index);
            if (configReader.getLoggerMap().get(nameCopy) != null) {
                return configReader.getLoggerMap().get(nameCopy);
            }
        }
        return configReader.getRoot();
    }
}
