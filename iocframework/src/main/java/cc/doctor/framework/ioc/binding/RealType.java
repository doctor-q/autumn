package cc.doctor.framework.ioc.binding;

/**
 * 解决java泛型擦除
 */
public class RealType {
    private Class rawType;

    public Class getRawType() {
        return rawType;
    }

    public void setRawType(Class rawType) {
        this.rawType = rawType;
    }

    public RealType(Class clazz) {
        this.rawType = clazz;
    }

    public static RealType get(Class clazz) {
        return null;
    }
}
