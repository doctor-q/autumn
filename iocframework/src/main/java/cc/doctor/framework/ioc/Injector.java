package cc.doctor.framework.ioc;

import cc.doctor.framework.ioc.binding.BindKey;
import cc.doctor.framework.ioc.binding.Binder;
import cc.doctor.framework.ioc.binding.BinderImpl;
import cc.doctor.framework.ioc.binding.Binding;
import cc.doctor.framework.ioc.scanner.Scanner;

import java.lang.annotation.Annotation;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class Injector {
    private Map<BindKey, Binding> bindingMap = new ConcurrentHashMap<>();
    private static Binder binder = new BinderImpl();
    private static int scanScope;
    private static Scanner scanner;

    public static void scan(String pack) {

    }

    /**
     * scan Inject and Provider annotations
     */
    public static Injector scan(Class clazz) {
        Injector injector = new Injector();
        scanner.scanClass(clazz, binder);
        return injector;
    }

    public static Injector createInjector(Binder binder) {
        return null;
    }

    public static <T> T getInstance(Class<T> tClass) {
        return null;
    }

    public static <T> T getInstance(Annotation annotation) {
        return null;
    }

    public static <T> T getInstance(BindKey bindKey) {
        return null;
    }

    public Binding getBinding(Class clazz) {
        return bindingMap.get(BindKey.get(clazz));
    }

    public Binding getBinding(BindKey bindKey) {
        return bindingMap.get(bindKey);
    }
}
