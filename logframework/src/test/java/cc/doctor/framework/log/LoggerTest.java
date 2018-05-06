package cc.doctor.framework.log;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class LoggerTest {
    private static final Logger log = LoggerFactory.getLogger(LoggerTest.class);
    @Test
    public void testLogger() {
        log.debug("debug");
        log.info("info");
        log.warn("warn");
        log.error("error");
    }
}
