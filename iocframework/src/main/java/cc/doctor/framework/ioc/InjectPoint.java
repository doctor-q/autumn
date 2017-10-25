package cc.doctor.framework.ioc;

import cc.doctor.framework.ioc.binding.RealType;

import java.lang.reflect.Member;

/**
 * 注入点，注入的第一步是发现所有注入点
 * 注入点可以是类型，构造器，属性和方法
 */
public class InjectPoint {
    private Member member;
    private RealType realType;

}
