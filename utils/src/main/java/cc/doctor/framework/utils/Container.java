package cc.doctor.framework.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by doctor on 2017/3/23.
 */
public class Container {
    private static final Logger log = LoggerFactory.getLogger(Container.class);
    public static final Container container = new Container();
    public static Map<String, Container> containerMap = new HashMap<>();
    public Map<String, Object> components = new ConcurrentHashMap<>();

    public static Container getContainer(String namespace) {
        if (namespace == null) {
            return container;
        }
        Container container = containerMap.get(namespace);
        if (container == null) {
            container = new Container();
            containerMap.put(namespace, container);
        }
        return container;
    }

    public void addComponent(Object object) {
        if (object != null) {
            components.put(object.getClass().getName(), object);
        }
    }

    public <T> T getComponent(Class<T> clazz) {
        if (clazz == null) {
            return null;
        }
        String clazzName = clazz.getName();
        return (T) getComponent(clazzName);
    }

    public Object getComponent(String name) {
        if (name == null) {
            return null;
        }
        return components.get(name);
    }

    public <T> T getOrCreateComponent(Class<T> tClass) {
        T component = getComponent(tClass);
        if (component == null) {
            try {
                component = tClass.newInstance();
                addComponent(component);
            } catch (InstantiationException | IllegalAccessException e) {
                log.error("", e);
            }
        }
        return component;
    }
}
