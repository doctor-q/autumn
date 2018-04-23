package cc.doctor.framework.validate;

import cc.doctor.framework.validate.annotation.*;

import java.lang.annotation.Annotation;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class ValidatorRegistry {

    private static ValidatorRegistry registry;

    private ValidatorRegistry() {

    }

    public static ValidatorRegistry getInstance() {
        if (registry == null) {
            registry = new ValidatorRegistry();
        }
        return registry;
    }

    private static Map<Class<? extends Annotation>, Validator> validatorMap = new ConcurrentHashMap<>();

    public static Validator get(Class<? extends Annotation> annotation) {
        return validatorMap.get(annotation);
    }

    public static <A extends Annotation> void register(Class<A> annotation, Validator<A> validator) {
        validatorMap.put(annotation, validator);
    }

    static {
        register(ArrayLength.class, ValidatorFactory.arrayLengthValidator());
        register(EnumIn.class, ValidatorFactory.enumInValidator());
        register(IntegerIn.class, ValidatorFactory.integerInValidator());
        register(IntegerNotIn.class, ValidatorFactory.integerNotInValidator());
        register(Ipv4.class, ValidatorFactory.ipv4Validator());
        register(Ipv6.class, ValidatorFactory.ipv6Validator());
        register(Mac.class, ValidatorFactory.macValidator());
        register(Max.class, ValidatorFactory.maxValidator());
        register(Min.class, ValidatorFactory.minValidator());
        register(NotEmpty.class, ValidatorFactory.notEmptyValidator());
        register(NotNull.class, ValidatorFactory.notNullValidator());
        register(NotNullEmpty.class, ValidatorFactory.notNullEmptyValidator());
        register(Pattern.class, ValidatorFactory.patternValidator());
        register(MaxMin.class, ValidatorFactory.rangeValidator());
        register(StringIn.class, ValidatorFactory.stringInValidator());
        register(StringLength.class, ValidatorFactory.stringLengthValidator());
        register(StringNotIn.class, ValidatorFactory.stringNotInValidator());
        register(ValidateInside.class, ValidatorFactory.validateInsideValidator());
    }
}
