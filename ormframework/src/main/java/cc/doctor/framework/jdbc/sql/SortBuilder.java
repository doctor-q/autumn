package cc.doctor.framework.jdbc.sql;

import java.util.List;

/**
 * Created by doctor on 2017/8/9.
 * build sorts
 */
public class SortBuilder implements Builder {
    private List<Sort> sorts;

    public List<Sort> getSorts() {
        return sorts;
    }

    public void setSorts(List<Sort> sorts) {
        this.sorts = sorts;
    }

    public static class Sort {
        private String field;
        private String order;
    }

    public SortBuilder addSort(Sort sort) {
        return this;
    }

    public SortBuilder addSort(String field, String order) {
        return this;
    }

    @Override
    public String build() {
        if (sorts == null || sorts.size() == 0) {
            return "";
        }
        StringBuilder orderBy = new StringBuilder(" order by ");
        for (Sort sort : sorts) {
            orderBy.append(sort.field).append(" ").append(sort.order).append(",");
        }
        return orderBy.substring(0, orderBy.length() - 1);
    }
}
