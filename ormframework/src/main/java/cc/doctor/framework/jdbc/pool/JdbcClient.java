package cc.doctor.framework.jdbc.pool;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.*;
import java.util.List;

/**
 * Created by doctor on 2017/7/12.
 */
public class JdbcClient {
    private static final Logger log = LoggerFactory.getLogger(JdbcClient.class);
    private String url;
    private String user;
    private String password;
    private Connection connection;

    public JdbcClient(String url, String user, String password) {
        this.url = url;
        this.user = user;
        this.password = password;
    }

    public boolean validate() {
        try {
            return connection.createStatement().executeQuery("select 1").next();
        } catch (SQLException e) {
            log.info("", e);
            return false;
        }
    }

    public void connect() {
        if (connection == null || !validate()) {
            try {
                Class.forName("com.mysql.jdbc.Driver");
                connection = DriverManager.getConnection(url, user, password);
            } catch (ClassNotFoundException | SQLException e) {
                log.error("", e);
            }
        }
    }

    public Connection getConnection() {
        return connection;
    }

    public Long execute(String sql, boolean generatedKeys) {
        connect();
        try {
            Statement statement = connection.createStatement();
            if (generatedKeys) {
                statement.execute(sql, Statement.RETURN_GENERATED_KEYS);
                ResultSet resultSet = statement.getGeneratedKeys();
                if (resultSet != null && resultSet.next()) {
                    return resultSet.getLong(1);
                }
            } else {
                statement.execute(sql);
            }
            statement.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public ResultSet executeQuery(String sql) {
        connect();
        try {
            Statement statement = connection.createStatement();
            return statement.executeQuery(sql);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public void executeBatch(List<String> sqls) {
        connect();
        try {
            Statement statement = connection.createStatement();
            for (String sql : sqls) {
                statement.addBatch(sql);
            }
            statement.executeBatch();
            statement.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
