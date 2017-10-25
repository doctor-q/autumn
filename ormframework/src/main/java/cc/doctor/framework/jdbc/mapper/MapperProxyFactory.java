package cc.doctor.framework.jdbc.mapper;

import cc.doctor.framework.jdbc.dao.SqlSessionFactory;

import java.lang.reflect.Proxy;

/**
 * Created by doctor on 2017/8/13.
 */
public class MapperProxyFactory {

    public static <T> T createMapper(Class<T> mapper, SqlSessionFactory sqlSessionFactory) {
        Class[] classes = {mapper};
        return (T) Proxy.newProxyInstance(MapperProxyFactory.class.getClassLoader(), classes, new MapperProxy(sqlSessionFactory));
    }
}
