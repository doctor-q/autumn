package cc.doctor.framework.validate;

import cc.doctor.framework.entity.Range;
import cc.doctor.framework.validate.annotation.*;

import java.util.*;

public class Validators {
    public static Validator<ArrayLength> arrayLengthValidator() {
        return new Validator<ArrayLength>() {
            @Override
            public boolean validate(ArrayLength annotation, String field, Object value) throws InvalidException {
                if (value == null) {
                    return true;
                }
                Range<Integer> range = new Range<>(annotation.min(), annotation.max());
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
                    throw new InvalidException(String.format("Array[%s] length=%s not in range%s", field, length, range.toRangeString()));
                }
                return true;
            }
        };
    }

    public static Validator<EnumIn> enumInValidator() {
        return new Validator<EnumIn>() {
            @Override
            public boolean validate(EnumIn annotation, String field, Object value) throws InvalidException {
                return false;
            }
        };
    }

    public static Validator<IntegerIn> integerInValidator() {
        return new Validator<IntegerIn>() {
            @Override
            public boolean validate(IntegerIn annotation, String field, Object value) throws InvalidException {
                if (value == null) {
                    return false;
                }
                int anInt = Integer.parseInt(value.toString());
                for (int i : annotation.value()) {
                    if (i == anInt) {
                        return true;
                    }
                }
                return false;
            }
        };
    }

    public static Validator<IntegerNotIn> integerNotInValidator() {
        return new Validator<IntegerNotIn>() {
            @Override
            public boolean validate(IntegerNotIn annotation, String field, Object value) throws InvalidException {
                if (value == null) {
                    return true;
                }
                int anInt = Integer.parseInt(value.toString());
                for (int i : annotation.value()) {
                    if (i == anInt) {
                        return false;
                    }
                }
                return true;
            }
        };
    }

    public static Validator<Ipv4> ipv4Validator() {
        return new Validator<Ipv4>() {
            @Override
            public boolean validate(Ipv4 annotation, String field, Object value) throws InvalidException {
                if (value == null) {
                    return false;
                }
                String[] split = value.toString().split("[.]");
                if (split.length != 4) {
                    return false;
                }
                for (String s : split) {
                    int anInt = Integer.parseInt(s);
                    if (anInt > 255 || anInt < 0) {
                        return false;
                    }
                }
                return true;
            }
        };
    }

    public static Validator<Ipv6> ipv6Validator() {
        return new Validator<Ipv6>() {
            @Override
            public boolean validate(Ipv6 annotation, String field, Object value) throws InvalidException {
                return false;
            }
        };
    }

    public static Validator<Mac> macValidator() {
        return new Validator<Mac>() {
            @Override
            public boolean validate(Mac annotation, String field, Object value) throws InvalidException {
                return false;
            }
        };
    }

    public static Validator<Max> maxValidator() {
        return new Validator<Max>() {
            @Override
            public boolean validate(Max annotation, String field, Object value) throws InvalidException {
                if (value == null) {
                    return false;
                }
                return Long.parseLong(value.toString()) <= annotation.value();
            }
        };
    }

    public static Validator<Min> minValidator() {
        return new Validator<Min>() {
            @Override
            public boolean validate(Min annotation, String field, Object value) throws InvalidException {
                if (value == null) {
                    return false;
                }
                return Long.parseLong(value.toString()) >= annotation.value();
            }
        };
    }

    public static Validator<NotEmpty> notEmptyValidator() {
        return new Validator<NotEmpty>() {
            @Override
            public boolean validate(NotEmpty annotation, String field, Object value) throws InvalidException {
                return false;
            }
        };
    }

    public static Validator<NotNull> notNullValidator() {
        return  new Validator<NotNull>() {
            @Override
            public boolean validate(NotNull annotation, String field, Object value) throws InvalidException {
                if (value == null) {
                    throw new InvalidException(String.format("Field[%s] is null", field));
                }
                return true;
            }
        };
    }

    public static Validator<NotNullEmpty> notNullEmptyValidator() {
        return new Validator<NotNullEmpty>() {
            @Override
            public boolean validate(NotNullEmpty annotation, String field, Object value) throws InvalidException {
                return false;
            }
        };
    }

    public static Validator<Pattern> patternValidator() {
        return new Validator<Pattern>() {
            @Override
            public boolean validate(Pattern annotation, String field, Object value) throws InvalidException {
                return false;
            }
        };
    }

    public static Validator<MaxMin> rangeValidator() {
        return new Validator<MaxMin>() {
            @Override
            public boolean validate(MaxMin annotation, String field, Object value) throws InvalidException {
                return false;
            }
        };
    }

    public static Validator<StringIn> stringInValidator() {
        return new Validator<StringIn>() {
            @Override
            public boolean validate(StringIn annotation, String field, Object value) throws InvalidException {
                if (value == null) {
                    return false;
                }
                for (String s : annotation.value()) {
                    if (s.equals(value.toString())) {
                        return true;
                    }
                }
                throw new InvalidException(String.format("Field[%s=%s] not in array%s", field, value, Arrays.toString(annotation.value())));
            }
        };
    }

    public static Validator<StringLength> stringLengthValidator() {
        return new Validator<StringLength>() {
            @Override
            public boolean validate(StringLength annotation, String field, Object value) throws InvalidException {
                if (value == null) {
                    return true;
                }
                Range<Integer> range = new Range<>(annotation.min(), annotation.max());
                if (!range.over(value.toString().length())) {
                    throw new InvalidException(String.format("Field[%s] length=%s not in range%s", field, value.toString().length(), range.toRangeString()));
                }
                return true;
            }
        };
    }

    public static Validator<StringNotIn> stringNotInValidator() {
        return new Validator<StringNotIn>() {
            @Override
            public boolean validate(StringNotIn annotation, String field, Object value) throws InvalidException {
                if (value == null) {
                    return true;
                }
                for (String s : annotation.value()) {
                    if (s.equals(value.toString())) {
                        return false;
                    }
                }
                return true;
            }
        };
    }

    public static Validator<ValidateInside> validateInsideValidator() {
        return new Validator<ValidateInside>() {
            @Override
            public boolean validate(ValidateInside annotation, String field, Object value) throws InvalidException {
                return false;
            }
        };
    }
}
