package cc.doctor.framework.jdbc.dao;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;

/**
 * Created by doctor on 2017/8/8.
 * create sql session
 */
public class SqlSessionFactory {
    public static final Logger log = LoggerFactory.getLogger(SqlSessionFactory.class);
    private DataSource dataSource;

    public SqlSessionFactory() {
    }

    public SqlSessionFactory(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    public DataSource getDataSource() {
        return dataSource;
    }

    public void setDataSource(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    public SqlSession createSqlSession() {
        try {
            Connection connection = dataSource.getConnection();
            return new AbstractSqlSession(connection);
        } catch (SQLException e) {
            log.error("", e);
        }
        return null;
    }
}
