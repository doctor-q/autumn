package cc.doctor.framework.jdbc.sql;

import cc.doctor.framework.utils.CollectionUtils;

import java.util.Collection;

/**
 * Created by doctor on 17-8-4.
 * query method support.
 */
public enum QueryMethod {
    EQUAL("equal", null) {
        @Override
        public String createCondition(String field, Object data, Boolean reverse) {
            if (reverse != null && reverse) {
                if (data == null) {
                    return field + " is not null";
                }
                return field + " != " + DynamicSqlBuilder.strQuotaJoinFilter.filter(data);
            }
            if (data == null) {
                return field + " is null";
            }
            return field + " = " + DynamicSqlBuilder.strQuotaJoinFilter.filter(data);
        }
    }, IN("in", Iterable.class) {
        @Override
        public String createCondition(String field, Object data, Boolean reverse) {
            String clause = null;
            if (data instanceof Iterable) {
                clause = CollectionUtils.join((Collection) data, ",", DynamicSqlBuilder.strQuotaJoinFilter);
                return field + " in (" + clause + ")";
            } else {
                return field + "='" + data + "'";
            }
        }
    }, RANGE("range", RangeQuery.class) {
        @Override
        public String createCondition(String field, Object data, Boolean reverse) {
            String query = "";
            RangeQuery rangeQuery = (RangeQuery)data;
            if (rangeQuery.left != null) {
                if (rangeQuery.leftClose) {
                    query += field + ">=" + DynamicSqlBuilder.strQuotaJoinFilter.filter(rangeQuery.left);
                } else {
                    query += field + ">" + DynamicSqlBuilder.strQuotaJoinFilter.filter(rangeQuery.left);
                }
            }
            if (rangeQuery.right != null) {
                if (!query.isEmpty()) {
                    query += " and ";
                }
                if (rangeQuery.rightClose) {
                    query += field + "<=" + DynamicSqlBuilder.strQuotaJoinFilter.filter(rangeQuery.right);
                } else {
                    query += field + "<" + DynamicSqlBuilder.strQuotaJoinFilter.filter(rangeQuery.right);
                }
            }
            return query;
        }
    },
    LIKE("like", LikeQuery.class) {
        @Override
        public String createCondition(String field, Object data, Boolean reverse) {
            LikeQuery likeQuery = (LikeQuery) data;
            String value = "";
            if (likeQuery.leftFuzzy) {
                value += "%";
            }
            value += likeQuery.keyword;
            if (likeQuery.rightFuzzy) {
                value += "%";
            }
            return field + " like " + DynamicSqlBuilder.strQuotaJoinFilter.filter(value);
        }
    };
    private String method;
    private Class queryEntity;

    QueryMethod(String method, Class queryEntity) {
        this.method = method;
        this.queryEntity = queryEntity;
    }

    public String getMethod() {
        return method;
    }

    public Class getQueryEntity() {
        return queryEntity;
    }

    public String createCondition(String field, Object data, Boolean reverse) {
        return null;
    }

    public static class RangeQuery {
        private Object left;
        private Boolean leftClose = false;
        private Object right;
        private Boolean rightClose = false;

        public Object getLeft() {
            return left;
        }

        public RangeQuery left(Object left) {
            this.left = left;
            return this;
        }

        public Boolean getLeftClose() {
            return leftClose;
        }

        public RangeQuery leftClose(Boolean leftClose) {
            this.leftClose = leftClose;
            return this;
        }

        public Object getRight() {
            return right;
        }

        public RangeQuery right(Object right) {
            this.right = right;
            return this;
        }

        public Boolean getRightClose() {
            return rightClose;
        }

        public RangeQuery setRightClose(Boolean rightClose) {
            this.rightClose = rightClose;
            return this;
        }
    }

    public static class LikeQuery {
        private Boolean leftFuzzy = false;
        private Boolean rightFuzzy = true;
        private String keyword;

        public LikeQuery() {
        }

        public LikeQuery(String keyword) {
            this.keyword = keyword;
        }

        public LikeQuery(Boolean leftFuzzy, String keyword) {
            this.leftFuzzy = leftFuzzy;
            this.keyword = keyword;
        }

        public LikeQuery(Boolean leftFuzzy, Boolean rightFuzzy, String keyword) {
            this.leftFuzzy = leftFuzzy;
            this.rightFuzzy = rightFuzzy;
            this.keyword = keyword;
        }

        public Boolean getLeftFuzzy() {
            return leftFuzzy;
        }

        public LikeQuery leftFuzzy(Boolean leftFuzzy) {
            this.leftFuzzy = leftFuzzy;
            return this;
        }

        public Boolean getRightFuzzy() {
            return rightFuzzy;
        }

        public LikeQuery rightFuzzy(Boolean rightFuzzy) {
            this.rightFuzzy = rightFuzzy;
            return this;
        }

        public String getKeyword() {
            return keyword;
        }

        public LikeQuery keyword(String keyword) {
            this.keyword = keyword;
            return this;
        }
    }

    public static QueryMethod getQueryMethod(String method) {
        if (method == null) {
            return null;
        }

        for (QueryMethod queryMethod : values()) {
            if (method.equals(queryMethod.getMethod())) {
                return queryMethod;
            }
        }

        return null;
    }
}
