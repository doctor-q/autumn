package cc.doctor.framework.jdbc.mapper;

import cc.doctor.framework.jdbc.annotation.insert.InsertIgnore;
import cc.doctor.framework.jdbc.annotation.insert.InsertNotSkipNull;
import cc.doctor.framework.jdbc.annotation.select.As;
import cc.doctor.framework.jdbc.annotation.select.Column;
import cc.doctor.framework.jdbc.annotation.select.ListIgnore;
import cc.doctor.framework.jdbc.annotation.select.Query;
import cc.doctor.framework.jdbc.sql.DataModel;

/**
 * Created by doctor on 2017/8/13.
 */
public class TestModel extends DataModel{
    @InsertIgnore
    private Long id;
    @Query
    @Column("username")
    @As("name")
    private String name;
    private Integer age;
    @ListIgnore
    private String desc;

    public TestModel() {
    }

    public TestModel(String name, int age) {
        this.name = name;
        this.age = age;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getAge() {
        return age;
    }

    public void setAge(Integer age) {
        this.age = age;
    }

    @Override
    public String toString() {
        return "TestModel{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", age=" + age +
                '}';
    }

    @Override
    public String table() {
        return "test";
    }
}
