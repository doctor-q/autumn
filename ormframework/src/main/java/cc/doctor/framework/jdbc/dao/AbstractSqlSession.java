package cc.doctor.framework.jdbc.dao;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;

/**
 * Created by doctor on 2017/8/8.
 */
public class AbstractSqlSession implements SqlSession {
    public static final Logger log = LoggerFactory.getLogger(AbstractSqlSession.class);
    private Connection connection;
    private boolean autoCommit;
    private boolean closed;

    public void setAutoCommit(boolean autoCommit) {
        this.autoCommit = autoCommit;
    }

    public AbstractSqlSession() {
    }

    public AbstractSqlSession(Connection connection) {
        this.connection = connection;
    }

    @Override
    public Connection getConnection() {
        return connection;
    }

    @Override
    public void setConnection(Connection connection) {
        this.connection = connection;
    }

    @Override
    public long insert(String sql, boolean generateKeys) {
        try {
            Statement statement = connection.createStatement();
            if (generateKeys) {
                statement.executeUpdate(sql, Statement.RETURN_GENERATED_KEYS);
                ResultSet generatedKeys = statement.getGeneratedKeys();
                if (generatedKeys.next()) {
                    return generatedKeys.getLong(1);
                }
            } else {
                log.debug(sql);
                statement.execute(sql);
            }
        } catch (SQLException e) {
            log.error("", e);
        }
        return 0;
    }

    @Override
    public int update(String sql) {
        try {
            Statement statement = connection.createStatement();
            log.debug(sql);
            statement.execute(sql);
        } catch (SQLException e) {
            log.error("", e);
        }
        return 0;
    }

    @Override
    public int delete(String sql) {
        try {
            Statement statement = connection.createStatement();
            log.debug(sql);
            statement.execute(sql);
        } catch (SQLException e) {
            log.error("", e);
        }
        return 0;
    }

    @Override
    public ResultSet select(String sql) {
        try {
            Statement statement = connection.createStatement();
            log.debug(sql);
            return statement.executeQuery(sql);
        } catch (SQLException e) {
            log.error("", e);
        }
        return null;
    }

    @Override
    public void commit() {
        try {
            connection.commit();
        } catch (SQLException e) {
            log.error("", e);
        }
    }

    @Override
    public void rollback() {
        try {
            connection.rollback();
        } catch (SQLException e) {
            log.error("", e);
        }
    }

    @Override
    public void close() {
        try {
            connection.close();
        } catch (SQLException e) {
            log.error("", e);
        }
        closed = false;
    }

    @Override
    public boolean isClosed() {
        return closed;
    }

    @Override
    public boolean isAutoCommit() {
        return autoCommit;
    }

    @Override
    public ResultSet execute(String sql) {
        return null;
    }

    @Override
    public void executeBatch(List<String> sqls) {
        setAutoCommit(false);
        try {
            Statement statement = connection.createStatement();
            for (String sql : sqls) {
                statement.addBatch(sql);
            }
            statement.executeBatch();
            statement.close();
            commit();
        } catch (SQLException e) {
            log.error("", e);
            rollback();
        }
    }
}
