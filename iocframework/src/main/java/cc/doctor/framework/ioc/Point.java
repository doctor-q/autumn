package cc.doctor.framework.ioc;

import cc.doctor.framework.ioc.binding.RealType;

import java.lang.reflect.Member;

public class Point {
    private Member member;
    private RealType realType;

    public Member getMember() {
        return member;
    }

    public void setMember(Member member) {
        this.member = member;
    }

    public RealType getRealType() {
        return realType;
    }

    public void setRealType(RealType realType) {
        this.realType = realType;
    }
}
