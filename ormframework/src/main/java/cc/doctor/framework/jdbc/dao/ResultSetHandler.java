package cc.doctor.framework.jdbc.dao;

import cc.doctor.framework.jdbc.sql.DataModel;
import cc.doctor.framework.utils.ReflectUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Field;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.*;

import static cc.doctor.framework.jdbc.sql.DynamicSqlBuilder.DYNAMIC_SQL_BUILDER;

/**
 * Created by doctor on 2017/8/8.
 */
public class ResultSetHandler {
    private static final Logger log = LoggerFactory.getLogger(ResultSetHandler.class);
    public static final ResultSetHandler resultSetHandler = new ResultSetHandler();

    public <D extends DataModel> List<D> convertDataModels(ResultSet resultSet, Class<D> dClass) {
        List<D> dataModels = new LinkedList<>();
        try {
            while (resultSet.next()) {
                D dataModel = dClass.newInstance();
                Map<String, Field> attrNameFields = ReflectUtils.getObjectAttrNameFields(dClass);
                for (String attribute : attrNameFields.keySet()) {
                    Field field = attrNameFields.get(attribute);
                    Object value = null;
                    Class<?> aClass = field.getType();
                    if (aClass.equals(Date.class)) {
                        Timestamp timestamp = resultSet.getObject(DYNAMIC_SQL_BUILDER.camelToUnderline(attribute), Timestamp.class);
                        if (timestamp != null) {
                            value = new Date(timestamp.getTime());
                        }
                    } else {
                        String column = DYNAMIC_SQL_BUILDER.camelToUnderline(attribute);
                        if (isExistColumn(resultSet, column)) {
                            value = resultSet.getObject(column, field.getType());
                            value = AnnotationHandler.handlerAll(value, field);
                        }
                    }
                    ReflectUtils.set(attribute, value, dataModel);
                }
                dataModels.add(dataModel);
            }
        } catch (Exception e) {
            log.error("", e);
            return null;
        }
        return dataModels;
    }

    public boolean isExistColumn(ResultSet rs, String columnName) {
        try {
            if (rs.findColumn(columnName) > 0) {
                return true;
            }
        } catch (SQLException e) {
            return false;
        }
        return false;
    }

    public List<Map<String, Object>> convertMaps(ResultSet resultSet) {
        List<Map<String, Object>> results = new LinkedList<>();
        try {
            while (resultSet.next()) {
                ResultSetMetaData metaData = resultSet.getMetaData();
                int columnCount = metaData.getColumnCount();
                Map<String, Object> map = new HashMap<>();
                for (int i = 1; i <= columnCount; i++) {
                    String columnLabel = metaData.getColumnLabel(i);
                    Object object = resultSet.getObject(i);
                    map.put(columnLabel, object);
                }
                results.add(map);
            }
        } catch (SQLException e) {
            log.error("", e);
        }
        return results;
    }
}
