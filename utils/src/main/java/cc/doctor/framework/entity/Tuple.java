package cc.doctor.framework.entity;

import java.io.Serializable;
import java.util.Objects;

/**
 * Created by doctor on 2017/7/17.
 */
public class Tuple<T1, T2> implements Serializable {
    private static final long serialVersionUID = 8794286401825326323L;
    private T1 t1;
    private T2 t2;

    public Tuple(T1 t1, T2 t2) {
        this.t1 = t1;
        this.t2 = t2;
    }

    public T1 getT1() {
        return t1;
    }

    public T2 getT2() {
        return t2;
    }

    @Override
    public String toString() {
        return "[" +
                "t1=" + t1 +
                ", t2=" + t2 +
                ']';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Tuple<?, ?> tuple = (Tuple<?, ?>) o;
        return Objects.equals(t1, tuple.t1) &&
                Objects.equals(t2, tuple.t2);
    }

    @Override
    public int hashCode() {

        return Objects.hash(t1, t2);
    }
}

