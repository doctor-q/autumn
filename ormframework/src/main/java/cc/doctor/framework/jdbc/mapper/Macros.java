package cc.doctor.framework.jdbc.mapper;

import cc.doctor.framework.jdbc.sql.DynamicSqlBuilder;
import cc.doctor.framework.utils.CollectionUtils;

import java.util.LinkedList;
import java.util.List;

/**
 * 二元宏
 * 宏格式：$xxx(column, param)
 */
public enum  Macros {
    IF_EMPTY("$ifEmpty") {
        @Override
        public String function(Object value, String right) {
            if (value != null && value.toString().isEmpty()) {
                return right;
            }
            return EMPTY;
        }
    },  //$ifEmpty(${name}, name=${name})
    IF_NOT_EMPTY("$ifNotEmpty") {
        @Override
        public String function(Object value, String right) {
            if (value == null || !value.toString().isEmpty()) {
                return right;
            }
            return EMPTY;
        }
    },  //$ifNotEmpty(${name}, name=${name})
    IF_NULL("$ifNull") {
        @Override
        public String function(Object value, String right) {
            if (value == null) {
                return right;
            }
            return EMPTY;
        }
    },  //$ifNull(${name}, name=${name})
    IF_NOT_NULL("$ifNotNull") {
        @Override
        public String function(Object value, String right) {
            if (value != null) {
                return right;
            }
            return EMPTY;
        }
    },  //$ifNotNull(${name}, name=${name})
    IF_EMPTY_OR_NULL("$ifEmptyOrNull") {
        @Override
        public String function(Object value, String right) {
            if (value == null || value.toString().isEmpty()) {
                return right;
            }
            return EMPTY;
        }
    },  //$ifEmptyOrNull(${name}, name=${name})
    IF_NOT_EMPTY_AND_NULL("$ifNotEmptyAndNull") {
        @Override
        public String function(Object value, String right) {
            if (value != null && !value.toString().isEmpty()) {
                return right;
            }
            return EMPTY;
        }
    },  //$ifNotEmptyAndNull(${name}, name=${name})
    JOIN("$join") {
        @Override
        public String function(Object value, String right) {
            List<Object> list = new LinkedList<>();
            if (value instanceof Iterable) {
                for (Object o : (Iterable<? extends Object>) value) {
                    list.add(DynamicSqlBuilder.strQuotaJoinFilter.filter(o));
                }
            } else if (value instanceof Object[]) {
                for (Object o : (Object[]) value) {
                    list.add(DynamicSqlBuilder.strQuotaJoinFilter.filter(o));
                }
            } else {
                list.add(DynamicSqlBuilder.strQuotaJoinFilter.filter(value));
            }
            return CollectionUtils.join(list, " " + right +" ");
        }
    };  //$join(list, ,)

    private String name;
    public static final String EMPTY = "";

    Macros(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public String function(Object value, String right) {
        return null;
    }
}
