package cc.doctor.framework.log;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class LoggerTest {
    private static final Logger log = LoggerFactory.getLogger(LoggerTest.class);
    private static final Logger fileLogger = LoggerFactory.getLogger("file_logger");
    @Test
    public void testLogger() {
        log(log);
        log(fileLogger);
    }

    private void log(Logger log) {
        log.debug("debug");
        log.info("info");
        log.warn("warn");
        log.error("error");
    }
}
