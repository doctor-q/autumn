package cc.doctor.framework.jdbc.sql;

import cc.doctor.framework.jdbc.annotation.select.SelectNotSkipNull;
import cc.doctor.framework.jdbc.annotation.select.Query;
import cc.doctor.framework.jdbc.dao.AnnotationHandler;
import cc.doctor.framework.utils.CollectionUtils;
import cc.doctor.framework.utils.ReflectUtils;

import java.lang.reflect.Field;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * Created by doctor on 2017/8/7.
 * build conditions
 */
public class QueryBuilder implements Builder {
    private Object queryBean;
    private List<QueryEntity> queryEntities = new LinkedList<>();

    public QueryBuilder() {
    }

    public QueryBuilder(Object queryBean) {
        this.queryBean = queryBean;
    }

    public Object getQueryBean() {
        return queryBean;
    }

    public void setQueryBean(Object queryBean) {
        this.queryBean = queryBean;
    }

    public void convertQueryBean(Object queryBean) {
        Class<?> queryBeanClass = queryBean.getClass();
        boolean notSkipNull = queryBeanClass.isAnnotationPresent(SelectNotSkipNull.class);
        Map<String, Field> attrNameFields = ReflectUtils.getObjectAttrNameFields(queryBeanClass);
        for (Field field : attrNameFields.values()) {
            if (field.isAnnotationPresent(Query.class)) {
                String column = field.getName();
                Query query = field.getAnnotation(Query.class);
                if (!query.field().isEmpty()) {
                    column = query.field();
                }
                boolean selectNotSkipNull = notSkipNull || field.isAnnotationPresent(SelectNotSkipNull.class);
                Object data = ReflectUtils.get(field.getName(), queryBean);
                if (data == null && !selectNotSkipNull) {
                    continue;
                }
                data = AnnotationHandler.handlerAll(data, field);
                queryEntities.add(new QueryEntity(QueryMethod.EQUAL.getMethod(), query.reverse(), column, data));
            }
        }
    }

    @Override
    public String build() {
        List<String> whereClause = new LinkedList<>();
        if (queryEntities.size() > 0) {
            for (QueryEntity queryEntity : queryEntities) {
                QueryMethod queryMethod = QueryMethod.getQueryMethod(queryEntity.method);
                String condition = queryMethod.createCondition(queryEntity.column, queryEntity.data, queryEntity.reverse);
                if (condition != null && !condition.isEmpty()) {
                    whereClause.add(condition);
                }
            }
        }
        if (whereClause.size() > 0) {
            return CollectionUtils.join(whereClause, " and ");
        }
        return "";
    }

    public QueryBuilder addQuery(QueryEntity queryEntity) {
        queryEntities.add(queryEntity);
        return this;
    }

    public static class QueryEntity {
        private String method;
        private boolean reverse;
        private String column;
        private Object data;

        public QueryEntity(String method, String column, Object data) {
            this.method = method;
            this.column = column;
            this.data = data;
        }

        public QueryEntity(String method, boolean reverse, String column, Object data) {
            this.method = method;
            this.reverse = reverse;
            this.column = column;
            this.data = data;
        }

        public String getMethod() {
            return method;
        }

        public void setMethod(String method) {
            this.method = method;
        }

        public boolean isReverse() {
            return reverse;
        }

        public void setReverse(boolean reverse) {
            this.reverse = reverse;
        }

        public String getColumn() {
            return column;
        }

        public void setColumn(String column) {
            this.column = column;
        }

        public Object getData() {
            return data;
        }

        public void setData(Object data) {
            this.data = data;
        }
    }
}
