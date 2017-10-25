package cc.doctor.framework.jdbc.mapper;


import cc.doctor.framework.jdbc.annotation.mapper.*;
import cc.doctor.framework.jdbc.dao.SqlSession;
import cc.doctor.framework.jdbc.dao.SqlSessionFactory;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.sql.ResultSet;
import java.util.HashMap;
import java.util.Map;

import static cc.doctor.framework.jdbc.dao.ResultSetHandler.resultSetHandler;

/**
 * Created by doctor on 2017/8/13.
 * mapper 代理
 */
public class MapperProxy implements InvocationHandler {
    private SqlSessionFactory sqlSessionFactory;

    public MapperProxy(SqlSessionFactory sqlSessionFactory) {
        this.sqlSessionFactory = sqlSessionFactory;
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        SqlSession sqlSession = sqlSessionFactory.createSqlSession();
        String template = null;
        //解析方法注解
        if (method.isAnnotationPresent(Insert.class)) {
            Insert insert = method.getAnnotation(Insert.class);
            template = insert.sql();
            if (method.isAnnotationPresent(ReturnPK.class)) {
                return sqlSession.insert(buildSql(template, method, args), true);
            } else {
                return sqlSession.insert(buildSql(template, method, args), false);
            }
        } else if (method.isAnnotationPresent(Delete.class)) {
            Delete delete = method.getAnnotation(Delete.class);
            template = delete.sql();
            return sqlSession.delete(buildSql(template, method, args));
        } else if (method.isAnnotationPresent(Update.class)) {
            Update update = method.getAnnotation(Update.class);
            template = update.sql();
            return sqlSession.update(buildSql(template, method, args));
        } else if (method.isAnnotationPresent(Select.class)) {
            Select select = method.getAnnotation(Select.class);
            template = select.sql();
            Class resultType = select.resultType();
            ResultSet resultSet = sqlSession.select(buildSql(template, method, args));
            return resultSetHandler.convertDataModels(resultSet, resultType);
        } else {
            throw new RuntimeException("Operation not support.");
        }
    }

    public String buildSql(String template, Method method, Object[] args) {
        String sql = template;

        if (args != null) {
            Class<?>[] parameterTypes = method.getParameterTypes();
            if (parameterTypes.length > 1) {
                Map<String, Object> datas = new HashMap<>();
                for (int i = 0; i < parameterTypes.length; i++) {
                    Class<?> parameterType = parameterTypes[i];
                    if (parameterType.isAnnotationPresent(Param.class)) {
                        Param param = parameterType.getAnnotation(Param.class);
                        datas.put(param.value(), args[i]);
                    }
                }
                sql = new SqlTemplateParser().parse(template, datas);
            } else {
                sql = new SqlTemplateParser().parse(template, args[0]);
            }
        }
        return sql;
    }
}
