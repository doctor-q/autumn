package cc.doctor.framework.entity;

/**
 * Created by doctor on 2017/7/17.
 */
public class Range<T> {
    private T left;
    private T right;
    private boolean leftClose = true;
    private boolean rightClose = true;

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
}

