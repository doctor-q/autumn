package cc.doctor.framework.jdbc.sql;

import cc.doctor.framework.jdbc.annotation.insert.InsertIgnore;
import cc.doctor.framework.jdbc.annotation.insert.InsertNotSkipNull;
import cc.doctor.framework.jdbc.annotation.select.As;
import cc.doctor.framework.jdbc.annotation.select.Column;
import cc.doctor.framework.jdbc.annotation.select.ListIgnore;
import cc.doctor.framework.jdbc.annotation.select.SelectIgnore;
import cc.doctor.framework.jdbc.annotation.update.UpdateIgnore;
import cc.doctor.framework.jdbc.annotation.update.UpdateNotSkipNull;
import cc.doctor.framework.jdbc.dao.AnnotationHandler;
import cc.doctor.framework.utils.CollectionUtils;
import cc.doctor.framework.utils.ReflectUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Field;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * Created by doctor on 2017/7/12.
 */
public class DynamicSqlBuilder {
    private static final Logger log = LoggerFactory.getLogger(DynamicSqlBuilder.class);
    public static final DynamicSqlBuilder DYNAMIC_SQL_BUILDER = new DynamicSqlBuilder();
    public static final CollectionUtils.JoinerFilter<String, String> colsJoinFilter = new CollectionUtils.JoinerFilter<String, String>() {

        @Override
        public String filter(String item) {
            return "`" + camelToUnderline(item) + "`";
        }
    };
    public static final CollectionUtils.JoinerFilter<Object, Object> strQuotaJoinFilter = new CollectionUtils.JoinerFilter<Object, Object>() {

        @Override
        public Object filter(Object item) {
            if (item instanceof String) {
                return "'" + item + "'";
            }
            return item;
        }
    };

    /**
     * return insert into test (`name`,`age`) values ('cc',12)
     */
    public String insertSql(DataModel dataModel) {
        Class<? extends DataModel> dataModelClass = dataModel.getClass();
        Map<String, Field> attrNameFields = ReflectUtils.getObjectAttrNameFields(dataModelClass);
        Map<String, Object> nameValueMap = new LinkedHashMap<>();
        boolean notSkipNull = dataModelClass.isAnnotationPresent(InsertNotSkipNull.class);
        for (Field field : attrNameFields.values()) {
            Object data = ReflectUtils.get(field.getName(), dataModel);
            if (field.isAnnotationPresent(InsertIgnore.class)) {
                continue;
            }
            boolean insertNotKipNull = field.isAnnotationPresent(InsertNotSkipNull.class) || notSkipNull;
            if (!insertNotKipNull && data == null) {
                continue;
            }
            nameValueMap.put(field.getName(), AnnotationHandler.handlerAll(data, field));
        }
        String colsClause = CollectionUtils.join(nameValueMap.keySet(), ",", colsJoinFilter);
        String valueClause = CollectionUtils.join(nameValueMap.values(), ",", strQuotaJoinFilter);
        return String.format("insert into %s (%s)values(%s)", dataModel.table(), colsClause, valueClause);
    }

    public static String camelToUnderline(String camel) {
        if (camel == null) {
            return null;
        }
        StringBuilder underline = new StringBuilder();
        for (int i = 0; i < camel.length(); i++) {
            if (Character.isUpperCase(camel.charAt(i))) {
                underline.append("_").append(Character.toLowerCase(camel.charAt(i)));
            } else {
                underline.append(camel.charAt(i));
            }
        }
        return underline.toString();
    }

    /**
     * return update test set `name`='cc',`age`=12 where id=1
     */
    public String updateByIdSql(final DataModel dataModel) {
        String setClause = updateColumnClause(dataModel);
        return String.format("update %s set %s where %s=%s",
                dataModel.table(), setClause, camelToUnderline(dataModel.generateId()), ReflectUtils.get(dataModel.generateId(), dataModel));
    }

    /**
     * return `name`='cc',`age`=12
     */
    public String updateColumnClause(final DataModel dataModel) {
        Map<String, Field> attrNameFields = ReflectUtils.getObjectAttrNameFields(dataModel.getClass());
        final Map<String, Object> nameValueMap = new LinkedHashMap<>();
        boolean notSkipNull = dataModel.getClass().isAnnotationPresent(InsertNotSkipNull.class);
        for (Field field : attrNameFields.values()) {
            Object data = ReflectUtils.get(field.getName(), dataModel);
            if (field.isAnnotationPresent(UpdateIgnore.class)) {
                continue;
            }
            boolean updateNotSkipNull = notSkipNull || field.isAnnotationPresent(UpdateNotSkipNull.class);
            if (!updateNotSkipNull && data == null) {
                continue;
            }
            nameValueMap.put(field.getName(), AnnotationHandler.handlerAll(data, field));
        }
        return CollectionUtils.join(nameValueMap.keySet(), ",", new CollectionUtils.JoinerFilter<String, String>() {
            @Override
            public String filter(String item) {
                return colsJoinFilter.filter(item) + "=" + strQuotaJoinFilter.filter(nameValueMap.get(item));
            }

            @Override
            public boolean apply(String value) {
                return !value.equals(dataModel.generateId());
            }
        });
    }

    /**
     * return select * from test where id=1
     */
    public String selectByIdSql(DataModel dataModel) {
        Object id = ReflectUtils.get(dataModel.generateId(), dataModel);
        return String.format("select * from %s where %s=%s", dataModel.table(), dataModel.generateId(), id);
    }

    /**
     * return delete from test where id=1
     */
    public String deleteByIdSql(DataModel dataModel) {
        Object id = ReflectUtils.get(dataModel.generateId(), dataModel);
        return String.format("delete from %s where %s=%s", dataModel.table(), dataModel.generateId(), id);
    }

    /**
     * return `name`, `age`, `address`
     */
    public <D extends DataModel> String selectColumnClause(Class<D> dClass, boolean list) {
        Map<String, Field> attrNameFields = ReflectUtils.getObjectAttrNameFields(dClass);
        List<String> columns = new LinkedList<>();
        for (Field field : attrNameFields.values()) {
            if (field.isAnnotationPresent(SelectIgnore.class)) {
                continue;
            }
            if (list && field.isAnnotationPresent(ListIgnore.class)) {
                continue;
            }
            String column = colsJoinFilter.filter(field.getName());
            if (field.isAnnotationPresent(Column.class)) {
                Column col = field.getAnnotation(Column.class);
                column = colsJoinFilter.filter(col.value());
            }
            if (field.isAnnotationPresent(As.class)) {
                As as = field.getAnnotation(As.class);
                column += " as " + as.value();
            }
            columns.add(column);
        }
        return CollectionUtils.join(columns, ",");
    }
}
