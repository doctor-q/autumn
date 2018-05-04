package cc.doctor.framework.log.event;

import java.util.Date;

public class DefaultEvent implements Event {
    private Level level;
    private String thread;
    private String message;
    private String formatMessage;
    private Object[] args;
    private Date logTime;
    private String logger;

    public Level getLevel() {
        return level;
    }

    public void setLevel(Level level) {
        this.level = level;
    }

    public String getThread() {
        return thread;
    }

    public void setThread(String thread) {
        this.thread = thread;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Object[] getArgs() {
        return args;
    }

    public void setArgs(Object[] args) {
        this.args = args;
    }

    public String getFormatMessage() {
        return formatMessage;
    }

    public void setFormatMessage(String formatMessage) {
        this.formatMessage = formatMessage;
    }

    public Date getLogTime() {
        return logTime;
    }

    public void setLogTime(Date logTime) {
        this.logTime = logTime;
    }

    public String getLogger() {
        return logger;
    }

    public void setLogger(String logger) {
        this.logger = logger;
    }

    public DefaultEvent level(Level level) {
        this.level = level;
        return this;
    }

    public DefaultEvent thread(String thread) {
        this.thread = thread;
        return this;
    }

    public DefaultEvent message(String message) {
        this.message = message;
        return this;
    }

    public DefaultEvent args(Object... args) {
        this.args = args;
        return this;
    }

    public DefaultEvent logTime(Date logTime) {
        this.logTime = logTime;
        return this;
    }

    public DefaultEvent logger(String logger) {
        this.logger = logger;
        return this;
    }
}
