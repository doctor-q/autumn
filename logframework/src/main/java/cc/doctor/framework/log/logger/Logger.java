package cc.doctor.framework.log.logger;

import cc.doctor.framework.log.appender.Appender;
import cc.doctor.framework.log.event.DefaultEvent;
import cc.doctor.framework.log.event.Event;
import cc.doctor.framework.log.event.Level;
import org.slf4j.Marker;

import java.util.Date;
import java.util.LinkedList;
import java.util.List;

public class Logger implements org.slf4j.Logger {
    private String name;
    private boolean additive = true;
    private List<Appender> appenders = new LinkedList<>();
    private Level level;
    private Logger root;

    public void setName(String name) {
        this.name = name;
    }

    public boolean isAdditive() {
        return additive;
    }

    public void setAdditive(boolean additive) {
        this.additive = additive;
    }

    public List<Appender> getAppenders() {
        return appenders;
    }

    public void setAppenders(List<Appender> appenders) {
        this.appenders = appenders;
    }

    public Level getLevel() {
        return level;
    }

    public void setLevel(Level level) {
        this.level = level;
    }

    public Logger getRoot() {
        return root;
    }

    public void setRoot(Logger root) {
        this.root = root;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public boolean isTraceEnabled() {
        return false;
    }

    @Override
    public void trace(String msg) {

    }

    @Override
    public void trace(String format, Object arg) {

    }

    @Override
    public void trace(String format, Object arg1, Object arg2) {

    }

    @Override
    public void trace(String format, Object... arguments) {

    }

    @Override
    public void trace(String msg, Throwable t) {

    }

    @Override
    public boolean isTraceEnabled(Marker marker) {
        return false;
    }

    @Override
    public void trace(Marker marker, String msg) {

    }

    @Override
    public void trace(Marker marker, String format, Object arg) {

    }

    @Override
    public void trace(Marker marker, String format, Object arg1, Object arg2) {

    }

    @Override
    public void trace(Marker marker, String format, Object... argArray) {

    }

    @Override
    public void trace(Marker marker, String msg, Throwable t) {

    }

    @Override
    public boolean isDebugEnabled() {
        return false;
    }

    @Override
    public void debug(String msg) {
        append(new DefaultEvent().logTime(new Date()).logger(getName())
                .thread(Thread.currentThread().getName()).message(msg));
    }

    @Override
    public void debug(String format, Object arg) {
        append(new DefaultEvent().logTime(new Date()).logger(getName())
                .thread(Thread.currentThread().getName()).message(format).args(arg));
    }

    @Override
    public void debug(String format, Object arg1, Object arg2) {
        append(new DefaultEvent().logTime(new Date()).logger(getName())
                .thread(Thread.currentThread().getName()).message(format).args(arg1, arg2));
    }

    @Override
    public void debug(String format, Object... arguments) {
        append(new DefaultEvent().logTime(new Date()).logger(getName())
                .thread(Thread.currentThread().getName()).message(format).args(arguments));
    }

    @Override
    public void debug(String msg, Throwable t) {
        append(new DefaultEvent().logTime(new Date()).logger(getName())
                .thread(Thread.currentThread().getName()).message(msg).args(t));
    }

    @Override
    public boolean isDebugEnabled(Marker marker) {
        return false;
    }

    @Override
    public void debug(Marker marker, String msg) {

    }

    @Override
    public void debug(Marker marker, String format, Object arg) {

    }

    @Override
    public void debug(Marker marker, String format, Object arg1, Object arg2) {

    }

    @Override
    public void debug(Marker marker, String format, Object... arguments) {

    }

    @Override
    public void debug(Marker marker, String msg, Throwable t) {

    }

    @Override
    public boolean isInfoEnabled() {
        return false;
    }

    @Override
    public void info(String msg) {
        append(new DefaultEvent().logTime(new Date()).logger(getName())
                .thread(Thread.currentThread().getName()).message(msg));
    }

    @Override
    public void info(String format, Object arg) {
        append(new DefaultEvent().logTime(new Date()).logger(getName())
                .thread(Thread.currentThread().getName()).message(format).args(arg));
    }

    @Override
    public void info(String format, Object arg1, Object arg2) {
        append(new DefaultEvent().logTime(new Date()).logger(getName())
                .thread(Thread.currentThread().getName()).message(format).args(arg1, arg2));
    }

    @Override
    public void info(String format, Object... arguments) {
        append(new DefaultEvent().logTime(new Date()).logger(getName())
                .thread(Thread.currentThread().getName()).message(format).args(arguments));
    }

    @Override
    public void info(String msg, Throwable t) {
        append(new DefaultEvent().logTime(new Date()).logger(getName())
                .thread(Thread.currentThread().getName()).message(msg).args(t));
    }

    @Override
    public boolean isInfoEnabled(Marker marker) {
        return false;
    }

    @Override
    public void info(Marker marker, String msg) {

    }

    @Override
    public void info(Marker marker, String format, Object arg) {

    }

    @Override
    public void info(Marker marker, String format, Object arg1, Object arg2) {

    }

    @Override
    public void info(Marker marker, String format, Object... arguments) {

    }

    @Override
    public void info(Marker marker, String msg, Throwable t) {

    }

    @Override
    public boolean isWarnEnabled() {
        return false;
    }

    @Override
    public void warn(String msg) {
        append(new DefaultEvent().logTime(new Date()).logger(getName())
                .thread(Thread.currentThread().getName()).message(msg));
    }

    @Override
    public void warn(String format, Object arg) {
        append(new DefaultEvent().logTime(new Date()).logger(getName())
                .thread(Thread.currentThread().getName()).message(format).args(arg));
    }

    @Override
    public void warn(String format, Object... arguments) {
        append(new DefaultEvent().logTime(new Date()).logger(getName())
                .thread(Thread.currentThread().getName()).message(format).args(arguments));
    }

    @Override
    public void warn(String format, Object arg1, Object arg2) {
        append(new DefaultEvent().logTime(new Date()).logger(getName())
                .thread(Thread.currentThread().getName()).message(format).args(arg1, arg2));
    }

    @Override
    public void warn(String msg, Throwable t) {
        append(new DefaultEvent().logTime(new Date()).logger(getName())
                .thread(Thread.currentThread().getName()).message(msg).args(t));
    }

    @Override
    public boolean isWarnEnabled(Marker marker) {
        return false;
    }

    @Override
    public void warn(Marker marker, String msg) {

    }

    @Override
    public void warn(Marker marker, String format, Object arg) {

    }

    @Override
    public void warn(Marker marker, String format, Object arg1, Object arg2) {

    }

    @Override
    public void warn(Marker marker, String format, Object... arguments) {

    }

    @Override
    public void warn(Marker marker, String msg, Throwable t) {

    }

    @Override
    public boolean isErrorEnabled() {
        return false;
    }

    @Override
    public void error(String msg) {
        append(new DefaultEvent().logTime(new Date()).logger(getName())
                .thread(Thread.currentThread().getName()).message(msg));
    }

    @Override
    public void error(String format, Object arg) {
        append(new DefaultEvent().logTime(new Date()).logger(getName())
                .thread(Thread.currentThread().getName()).message(format).args(arg));
    }

    @Override
    public void error(String format, Object arg1, Object arg2) {
        append(new DefaultEvent().logTime(new Date()).logger(getName())
                .thread(Thread.currentThread().getName()).message(format).args(arg1, arg2));
    }

    @Override
    public void error(String format, Object... arguments) {
        append(new DefaultEvent().logTime(new Date()).logger(getName())
                .thread(Thread.currentThread().getName()).message(format).args(arguments));
    }

    @Override
    public void error(String msg, Throwable t) {
        append(new DefaultEvent().logTime(new Date()).logger(getName())
                .thread(Thread.currentThread().getName()).message(msg).args(t));
    }

    @Override
    public boolean isErrorEnabled(Marker marker) {
        return false;
    }

    @Override
    public void error(Marker marker, String msg) {

    }

    @Override
    public void error(Marker marker, String format, Object arg) {

    }

    @Override
    public void error(Marker marker, String format, Object arg1, Object arg2) {

    }

    @Override
    public void error(Marker marker, String format, Object... arguments) {

    }

    @Override
    public void error(Marker marker, String msg, Throwable t) {

    }

    private void append(Event event) {
        for (Appender appender : appenders) {
            if (event.getLevel().above(this.level)) {
                appender.append(event);
            }
        }
        if (additive) {
            root.append(event);
        }
    }

    public void addAppender(Appender appender) {
        appenders.add(appender);
    }
}
