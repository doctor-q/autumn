package cc.doctor.framework.jdbc.dao;

import java.sql.Connection;
import java.sql.ResultSet;
import java.util.List;

/**
 * Created by doctor on 2017/8/8.
 * sql session execute real sql and return result
 */
public interface SqlSession {
    Connection getConnection();
    void setConnection(Connection connection);
    long insert(String sql, boolean generateKeys);
    int update(String sql);
    int delete(String sql);
    ResultSet select(String sql);
    void commit();
    void rollback();
    void close();
    boolean isClosed();
    boolean isAutoCommit();
    void setAutoCommit(boolean autoCommit);

    ResultSet execute(String sql);

    void executeBatch(List<String> sqls);
}
