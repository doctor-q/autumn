package cc.doctor.framework.jdbc.sql;

/**
 * Created by doctor on 2017/8/9.
 * normal functions
 */
public enum SqlFunctions {
    ABS("abs"),
    AVG("avg"),
    COUNT("count"),
    FIRST("first"),
    LAST("last"),
    MAX("max"),
    MIN("min"),
    SUM("sum"),
    UCASE("ucase"),
    LCASE("lcase"),
    LEN("len");
    private String name;

    SqlFunctions(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public String wrapper(String field) {
        return String.format("%s(%s)", name, field);
    }

    public static SqlFunctions get(String function) {
        for (SqlFunctions sqlFunctions : values()) {
            if (sqlFunctions.name.equals(function)) {
                return sqlFunctions;
            }
        }
        return null;
    }
}
