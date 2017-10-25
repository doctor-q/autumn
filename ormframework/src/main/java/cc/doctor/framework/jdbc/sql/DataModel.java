package cc.doctor.framework.jdbc.sql;

/**
 * Created by doctor on 2017/7/12.
 * every db model extends this
 */
public abstract class DataModel {
    public String generateId() {
        return "id";
    }

    public abstract String table();
}
