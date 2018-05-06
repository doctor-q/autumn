package cc.doctor.framework.log.config;

import cc.doctor.framework.log.appender.Appender;
import cc.doctor.framework.log.logger.Logger;

import java.util.Map;

public interface ConfigReader {
    void load();
    Map<String, Appender> getAppenderMap();
    Map<String, Logger> getLoggerMap();
    Logger getRoot();
}
