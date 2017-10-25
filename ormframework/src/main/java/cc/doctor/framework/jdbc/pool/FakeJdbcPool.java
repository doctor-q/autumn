package cc.doctor.framework.jdbc.pool;

import javax.sql.DataSource;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.SQLFeatureNotSupportedException;

/**
 * Created by doctor on 2017/7/12.
 */
public class FakeJdbcPool implements DataSource {
    private JdbcClient jdbcClient;
    private JdbcPoolConfig jdbcPoolConfig;

    public FakeJdbcPool(String url, String user, String password) {
        jdbcClient = new JdbcClient(url, user, password);
    }

    @Override
    public Connection getConnection() throws SQLException {
        jdbcClient.connect();
        return jdbcClient.getConnection();
    }

    @Override
    public Connection getConnection(String username, String password) throws SQLException {
        return jdbcClient.getConnection();
    }

    @Override
    public <T> T unwrap(Class<T> iface) throws SQLException {
        return null;
    }

    @Override
    public boolean isWrapperFor(Class<?> iface) throws SQLException {
        return false;
    }

    @Override
    public PrintWriter getLogWriter() throws SQLException {
        return null;
    }

    @Override
    public void setLogWriter(PrintWriter out) throws SQLException {

    }

    @Override
    public void setLoginTimeout(int seconds) throws SQLException {

    }

    @Override
    public int getLoginTimeout() throws SQLException {
        return 0;
    }

    @Override
    public java.util.logging.Logger getParentLogger() throws SQLFeatureNotSupportedException {
        return null;
    }
}
