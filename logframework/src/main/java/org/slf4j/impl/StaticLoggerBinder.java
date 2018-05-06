package org.slf4j.impl;

import cc.doctor.framework.log.LoggerFactory;
import org.slf4j.ILoggerFactory;
import org.slf4j.spi.LoggerFactoryBinder;

public class StaticLoggerBinder implements LoggerFactoryBinder {
    private static StaticLoggerBinder INSTANCE;

    public static StaticLoggerBinder getSingleton() {
        if (INSTANCE == null) {
            INSTANCE = new StaticLoggerBinder();
        }
        return INSTANCE;
    }

    @Override
    public ILoggerFactory getLoggerFactory() {
        return LoggerFactory.getSingleton();
    }

    @Override
    public String getLoggerFactoryClassStr() {
        return LoggerFactory.class.getName();
    }

}
