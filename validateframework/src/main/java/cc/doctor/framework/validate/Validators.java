package cc.doctor.framework.validate;

import cc.doctor.framework.entity.Range;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Collection;
import java.util.Map;

public class Validators {
    private static final Logger log = LoggerFactory.getLogger(Validators.class);

    public static void validateArrayLength(Object value, Integer min, Integer max) throws InvalidException {
        Range<Integer> range = new Range<>(min, max);
        int length;
        if (value instanceof Collection) {
            length = ((Collection) value).size();
        } else if (value instanceof Map) {
            length = ((Map) value).keySet().size();
        } else if (value instanceof Object[]) {
            length = ((Object[]) value).length;
        } else {
            length = 1;
        }
        if (!range.over(length)) {
            throw new InvalidException(String.format("Array length=%s not in range%s", length, range.toRangeString()));
        }
    }

    public static void validateEnumIn(Object value, Class enumClass) throws InvalidException {
        if (value == null) {
            throw new NullValueException();
        }
        try {
            Method method = enumClass.getMethod("values");
            Object[] invoke = (Object[]) method.invoke(null, null);
            for (Object object : invoke) {
                if (value.equals(object)) {
                    return;
                }
            }
            throw new InvalidException(String.format("Value %s not in enum %s", value, enumClass.getName()));
        } catch (Exception e) {
            log.error("", e);
        }
    }

    public static void validateIntegerIn(Object value, int[] values) throws InvalidException {
        if (value == null) {
            throw new NullValueException();
        }
        int anInt = Integer.parseInt(value.toString());
        for (int i : values) {
            if (i == anInt) {
                return;
            }
        }
        throw new InvalidException(String.format("Value[%s] not in %s", value, Arrays.toString(values)));
    }

    public static void validateIntegerNotIn(Object value, int[] values) throws InvalidException {
        if (value == null) {
            return;
        }
        int anInt = Integer.parseInt(value.toString());
        for (int i : values) {
            if (i == anInt) {
                throw new InvalidException(String.format("Value[%s] in %s", value, Arrays.toString(values)));
            }
        }
    }

    public static void validateStringIn(Object value, String[] strings) throws InvalidException {
        if (value == null) {
            throw new NullValueException();
        }
        for (String s : strings) {
            if (s.equals(value.toString())) {
                return;
            }
        }
        throw new InvalidException(String.format("Value[%s] not in array%s", value, Arrays.toString(strings)));

    }

    public static void validateStringNotIn(Object value, String[] strings) throws InvalidException {
        if (value == null) {
            return;
        }
        for (String s : strings) {
            if (s.equals(value.toString())) {
                throw new InvalidException(String.format("Value %s in array %s", value, Arrays.toString(strings)));
            }
        }
    }

    public static void validateIpv4(Object value) throws InvalidException {
        if (value == null) {
            throw new NullValueException();
        }
        String[] split = value.toString().split("[.]");
        if (split.length != 4) {
            throw new InvalidException(String.format("Value %s not match ipv4 format", value));
        }
        for (String s : split) {
            int anInt = Integer.parseInt(s);
            if (anInt > 255 || anInt < 0) {
                throw new InvalidException(String.format("Value %s not match ipv4 format", value));
            }
        }
    }

    public static void validateIpv6(Object value) throws InvalidException {
        if (value == null) {
            throw new NullValueException();
        }

        String address = value.toString();
        boolean result = false;
        String regHex = "(\\p{XDigit}{1,4})";
        //没有双冒号
        String regIPv6Full = "^(" + regHex + ":){7}" + regHex + "$";
        //双冒号在中间或者没有双冒号
        String regIPv6AbWithColon = "^(" + regHex + "(:|::)){0,6}" + regHex + "$";
        //双冒号开头
        String regIPv6AbStartWithDoubleColon = "^(" + "::(" + regHex + ":){0,5}" + regHex + ")$";
        String regIPv6 = "^(" + regIPv6Full + ")|("
                + regIPv6AbStartWithDoubleColon + ")|(" + regIPv6AbWithColon + ")$";
        //下面还要处理地址为::的情形和地址包含多于一个的::的情况（非法）
        if (address.contains(":")) {
            if (address.length() <= 39) {
                String addressTemp = address;
                int doubleColon = 0;
                if (address.equals("::")) {
                    return;
                }
                while (addressTemp.contains("::")) {
                    addressTemp = addressTemp.substring(addressTemp
                            .indexOf("::") + 2, addressTemp.length());
                    doubleColon++;
                }
                if (doubleColon <= 1) {
                    result = address.matches(regIPv6);
                }
            }
        }
        if (!result) {
            throw new InvalidException(String.format("Value %s not match ipv6 format", value));
        }
    }

    public static void validateMacAddress(Object value) throws InvalidException {
        if (value == null) {
            throw new NullValueException();
        }

        String trueMacAddress = "([A-Fa-f0-9]{2}-){5}[A-Fa-f0-9]{2}";
        if (!value.toString().matches(trueMacAddress)) {
            throw new InvalidException(String.format("Value %s not match mac address format", value));
        }
    }

    public static void validateMax(Object value, double max) throws InvalidException {
        if (value == null) {
            throw new NullValueException();
        }
        if (Double.parseDouble(value.toString()) > max) {
            throw new InvalidException(String.format("Value %s more than %s", value, max));
        }
    }

    public static void validateMin(Object value, double min) throws InvalidException {
        if (value == null) {
            throw new NullValueException();
        }
        if (Double.parseDouble(value.toString()) < min) {
            throw new InvalidException(String.format("Value %s less than %s", value, min));
        }
    }

    public static void validateMaxMin(Object value, double min, double max) throws InvalidException {
        if (value == null) {
            throw new NullValueException();
        }
        Range<Double> doubleRange = new Range<>(min, max);
        if (!doubleRange.over(Double.parseDouble(value.toString()))) {
            throw new InvalidException(String.format("Value %s not in range [%s, %s]", value, min, max));
        }
    }

    public static void validateEmpty(Object value) throws InvalidException {
        if (value != null && value.toString().isEmpty()) {
            throw new InvalidException(String.format("Value %s is not empty", value));
        }
    }

    public static void validateNotEmpty(Object value) throws InvalidException {
        if (value != null && !value.toString().isEmpty()) {
            throw new InvalidException(String.format("Value %s is empty", value));
        }
    }

    public static void validateNull(Object value) throws InvalidException {
        if (value != null) {
            throw new InvalidException("Value is not null");
        }
    }

    public static void validateNotNull(Object value) throws InvalidException {
        if (value == null) {
            throw new InvalidException("Value is null");
        }
    }

    public static void validateRegexp(Object value, String regexp) throws InvalidException {
        if (value == null) {
            throw new NullValueException();
        }
        if (!value.toString().matches(regexp)) {
            throw new InvalidException(String.format("Value %s not match expression %s", value, regexp));
        }
    }

    public static void validateStringLength(Object value, int min, int max) throws InvalidException {
        if (value == null) {
            return;
        }
        Range<Integer> range = new Range<>(min, max);
        if (!range.over(value.toString().length())) {
            throw new InvalidException(String.format("Value[%s] length=%s not in range%s", value, value.toString().length(), range.toRangeString()));
        }
    }
}
