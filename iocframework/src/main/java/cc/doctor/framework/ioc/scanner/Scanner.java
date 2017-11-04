package cc.doctor.framework.ioc.scanner;

import cc.doctor.framework.ioc.Inject;
import cc.doctor.framework.ioc.InjectPoint;
import cc.doctor.framework.ioc.Provider;
import cc.doctor.framework.ioc.ProviderPoint;
import cc.doctor.framework.ioc.binding.Binder;
import cc.doctor.framework.ioc.binding.RealType;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

public class Scanner {
    private int scanScope;
    public void scanPackage(String pack) {

    }

    public void scanClass(Class clazz, Binder binder) {
        if ((scanScope & ScanScope.TYPE) > 0) {
            if (clazz.isAnnotationPresent(Provider.class)) {
                Provider provider = (Provider) clazz.getAnnotation(Provider.class);
                Class aClass = provider.value();
                binder.bind(aClass).to(clazz);
            }
        }
        if ((scanScope & ScanScope.FIELD) > 0) {
            Field[] declaredFields = clazz.getDeclaredFields();
            for (Field declaredField : declaredFields) {
                if (clazz.isAnnotationPresent(Inject.class)) {
                    InjectPoint injectPoint = new InjectPoint();
                    injectPoint.setRealType(RealType.get(clazz));
                    injectPoint.setMember(declaredField);
                }
            }
        }
        if ((scanScope & ScanScope.METHOD) > 0) {
            Method[] methods = clazz.getMethods();
            for (Method method : methods) {
                if (method.isAnnotationPresent(Provider.class)) {
                    Provider provider = method.getAnnotation(Provider.class);
                    ProviderPoint providerPoint = new ProviderPoint();
                    providerPoint.setMember(method);
                    providerPoint.setRealType(RealType.get(clazz));
                    binder.bind(provider.value()).to(providerPoint);
                }
            }
        }
    }
}
