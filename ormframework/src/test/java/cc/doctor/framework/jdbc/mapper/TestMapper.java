package cc.doctor.framework.jdbc.mapper;

import cc.doctor.framework.jdbc.annotation.mapper.*;

import java.util.List;

/**
 * Created by doctor on 2017/8/13.
 */
@Mapper
public interface TestMapper {
    @Select(
            sql = "select * from test where id=${id}",
            resultType = TestModel.class
    )
    List<TestModel> selectTest(@Param("id") long id);

    @Select(
            sql = "select * from test $ifNotNull(${ids}, where id in ($join(${id}, ,)))",
            resultType = TestModel.class
    )
    List<TestModel> selectInTest(List<Long> ids);

    @Insert(
            sql = "insert into test (name, age) values ('${name}', ${age})"
    )
    @ReturnPK
    long insertTest(TestModel testModel);

    @Delete(
            sql = "delete from test where id=${id}"
    )
    void deleteTest(Long id);

    @Update(
            sql = "update test set name='${name}', age=${age} where id=${id}"
    )
    void updateTest(TestModel testModel);
}
