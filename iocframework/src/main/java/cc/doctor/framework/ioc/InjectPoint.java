package cc.doctor.framework.ioc;


import cc.doctor.framework.ioc.dependency.Dependency;

import java.util.List;

/**
 * 注入点，注入的第一步是发现所有注入点
 * 注入点可以是类型，构造器，属性和方法
 */
public class InjectPoint extends Point {
    List<Dependency> dependencies;
}
