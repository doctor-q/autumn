package cc.doctor.framework.validate;

import cc.doctor.framework.validate.annotation.*;

public class ValidatorFactory {
    public static Validator<ArrayLength> arrayLengthValidator() {
        return new Validator<ArrayLength>() {
            @Override
            public void validate(ArrayLength annotation, Object value) throws InvalidException {
                if (value == null) {
                    return;
                }
                Validators.validateArrayLength(value, annotation.min(), annotation.max());
            }
        };
    }

    public static Validator<EnumIn> enumInValidator() {
        return new Validator<EnumIn>() {
            @Override
            public void validate(EnumIn annotation, Object value) throws InvalidException {
                Validators.validateEnumIn(value, annotation.value());
            }
        };
    }

    public static Validator<IntegerIn> integerInValidator() {
        return new Validator<IntegerIn>() {
            @Override
            public void validate(IntegerIn annotation, Object value) throws InvalidException {
                Validators.validateIntegerIn(value, annotation.value());
            }
        };
    }

    public static Validator<IntegerNotIn> integerNotInValidator() {
        return new Validator<IntegerNotIn>() {
            @Override
            public void validate(IntegerNotIn annotation, Object value) throws InvalidException {
                Validators.validateIntegerNotIn(value, annotation.value());
            }
        };
    }

    public static Validator<Ipv4> ipv4Validator() {
        return new Validator<Ipv4>() {
            @Override
            public void validate(Ipv4 annotation, Object value) throws InvalidException {
                Validators.validateIpv4(value);
            }
        };
    }

    public static Validator<Ipv6> ipv6Validator() {
        return new Validator<Ipv6>() {
            @Override
            public void validate(Ipv6 annotation, Object value) throws InvalidException {
                Validators.validateIpv6(value);
            }
        };
    }

    public static Validator<Mac> macValidator() {
        return new Validator<Mac>() {
            @Override
            public void validate(Mac annotation, Object value) throws InvalidException {
                Validators.validateMacAddress(value);
            }
        };
    }

    public static Validator<Max> maxValidator() {
        return new Validator<Max>() {
            @Override
            public void validate(Max annotation, Object value) throws InvalidException {
                Validators.validateMax(value, annotation.value());
            }
        };
    }

    public static Validator<Min> minValidator() {
        return new Validator<Min>() {
            @Override
            public void validate(Min annotation, Object value) throws InvalidException {
                Validators.validateMin(value, annotation.value());
            }
        };
    }

    public static Validator<NotEmpty> notEmptyValidator() {
        return new Validator<NotEmpty>() {
            @Override
            public void validate(NotEmpty annotation, Object value) throws InvalidException {
                Validators.validateNotEmpty(value);
            }
        };
    }

    public static Validator<NotNull> notNullValidator() {
        return new Validator<NotNull>() {
            @Override
            public void validate(NotNull annotation, Object value) throws InvalidException {
                Validators.validateNotNull(value);
            }
        };
    }

    public static Validator<NotNullEmpty> notNullEmptyValidator() {
        return new Validator<NotNullEmpty>() {
            @Override
            public void validate(NotNullEmpty annotation, Object value) throws InvalidException {
                Validators.validateNotNull(value);
                Validators.validateNotEmpty(value);
            }
        };
    }

    public static Validator<Pattern> patternValidator() {
        return new Validator<Pattern>() {
            @Override
            public void validate(Pattern annotation, Object value) throws InvalidException {
                Validators.validateRegexp(value, annotation.value());
            }
        };
    }

    public static Validator<MaxMin> rangeValidator() {
        return new Validator<MaxMin>() {
            @Override
            public void validate(MaxMin annotation, Object value) throws InvalidException {
                Validators.validateMaxMin(value, annotation.min(), annotation.max());
            }
        };
    }

    public static Validator<StringIn> stringInValidator() {
        return new Validator<StringIn>() {
            @Override
            public void validate(StringIn annotation, Object value) throws InvalidException {
                Validators.validateStringIn(value, annotation.value());
            }
        };
    }

    public static Validator<StringLength> stringLengthValidator() {
        return new Validator<StringLength>() {
            @Override
            public void validate(StringLength annotation, Object value) throws InvalidException {
                Validators.validateStringLength(value, annotation.min(), annotation.max());
            }
        };
    }

    public static Validator<StringNotIn> stringNotInValidator() {
        return new Validator<StringNotIn>() {
            @Override
            public void validate(StringNotIn annotation, Object value) throws InvalidException {
                Validators.validateStringNotIn(value, annotation.value());
            }
        };
    }

    public static Validator<ValidateInside> validateInsideValidator() {
        return new Validator<ValidateInside>() {
            @Override
            public void validate(ValidateInside annotation, Object value) throws InvalidException {

            }
        };
    }
}
