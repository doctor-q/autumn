package org.slf4j.impl;

import org.slf4j.ILoggerFactory;
import org.slf4j.spi.LoggerFactoryBinder;

public class StaticLoggerBinder implements LoggerFactoryBinder {


    public ILoggerFactory getLoggerFactory() {
        return null;
    }

    @Override
    public String getLoggerFactoryClassStr() {
        return null;
    }

}
