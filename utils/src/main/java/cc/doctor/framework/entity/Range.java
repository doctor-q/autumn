package cc.doctor.framework.entity;

/**
 * Created by doctor on 2017/7/17.
 */
public class Range<T extends Comparable> {
    private T left;
    private T right;
    private boolean leftClose = true;
    private boolean rightClose = true;

    public Range() {
    }

    public Range(T left, T right) {
        this.left = left;
        this.right = right;
    }

    public Range(T left, T right, boolean leftClose, boolean rightClose) {
        this.left = left;
        this.right = right;
        this.leftClose = leftClose;
        this.rightClose = rightClose;
    }

    public T getLeft() {
        return left;
    }

    public void setLeft(T left) {
        this.left = left;
    }

    public T getRight() {
        return right;
    }

    public void setRight(T right) {
        this.right = right;
    }

    public boolean isLeftClose() {
        return leftClose;
    }

    public void setLeftClose(boolean leftClose) {
        this.leftClose = leftClose;
    }

    public boolean isRightClose() {
        return rightClose;
    }

    public void setRightClose(boolean rightClose) {
        this.rightClose = rightClose;
    }

    public static Range convertRange(String range) {
        range = range.replace(" ","");
        Range r = new Range();
        if (range.startsWith("(")) {
            r.setLeftClose(false);
        } else if (range.startsWith("[")) {
            r.setLeftClose(true);
        }
        range = range.substring(1);
        if (range.endsWith(")")) {
            r.setRightClose(false);
        } else if (range.endsWith("]")) {
            r.setRightClose(true);
        }
        range = range.substring(0, range.length() - 1);
        String[] split = range.split(",");
        r.setLeft(split[0] == null ? null : Double.parseDouble(split[0]));
        r.setRight(split[1] == null ? null : Double.parseDouble(split[1]));
        return r;
    }

    public boolean over(T number) {
        boolean over = true;
        if (this.left != null) {
            over = leftClose ? number.compareTo(left) >= 0 : number.compareTo(left) > 0;
        }
        if (this.right != null) {
            over = over && (rightClose ? number.compareTo(right) <= 0 : number.compareTo(right) < 0);
        }
        return over;
    }

    public String toRangeString() {
        String rangeString = "";
        if (leftClose) {
            rangeString += "[";
        } else {
            rangeString += "(";
        }
        if (left != null) {
            rangeString += left;
        }
        rangeString += ",";
        if (right != null) {
            rangeString += right;
        }
        if (rightClose) {
            rangeString += "]";
        } else {
            rangeString += ")";
        }
        return rangeString;
    }
}

