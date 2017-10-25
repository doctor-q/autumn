package cc.doctor.framework.jdbc.sql;

/**
 * Created by doctor on 2017/8/8.
 */
public class OffsetLimitBuilder implements Builder {
    private Long offset;
    private Long limit;

    public OffsetLimitBuilder() {
    }

    public OffsetLimitBuilder(Long limit) {
        this.limit = limit;
    }

    public OffsetLimitBuilder(Long offset, Long limit) {
        this.offset = offset;
        this.limit = limit;
    }

    public Long getOffset() {
        return offset;
    }

    public void setOffset(Long offset) {
        this.offset = offset;
    }

    public Long getLimit() {
        return limit;
    }

    public void setLimit(Long limit) {
        this.limit = limit;
    }

    @Override
    public String build() {
        if (limit == null) {
            return null;
        }
        if (offset == null) {
            return String.format(" limit %s", limit);
        }
        return String.format(" limit %s, %s", offset, limit);
    }
}
