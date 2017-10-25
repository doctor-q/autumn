package cc.doctor.framework.jdbc.mapper;

import cc.doctor.framework.jdbc.dao.SqlSessionFactory;
import cc.doctor.framework.jdbc.pool.FakeJdbcPool;
import org.junit.Before;
import org.junit.Test;

import javax.sql.DataSource;
import java.util.List;

/**
 * Created by doctor on 2017/8/13.
 */
public class MapperProxyFactoryTest {
    private DataSource dataSource;
    private SqlSessionFactory sqlSessionFactory;
    private TestMapper mapper;

    @Before
    public void setUp() {
        dataSource = new FakeJdbcPool(
                "jdbc:mysql://127.0.0.1:3306/brilliant?useUnicode=true&characterEncoding=utf-8", "root", "123456"
        );
        sqlSessionFactory = new SqlSessionFactory(dataSource);
        mapper = MapperProxyFactory.createMapper(TestMapper.class, sqlSessionFactory);
    }

    @Test
    public void selectTest() throws Exception {
        List<TestModel> testModel = mapper.selectTest(1);
        System.out.println(testModel);
    }

    @Test
    public void insertTest() {
        for (int i = 0; i < 10; i++) {
            TestModel testModel = new TestModel("name" + i, 10 + i);
            long pk = mapper.insertTest(testModel);
            System.out.println(pk);
        }
    }

    @Test
    public void updateTest() {
        TestModel testModel = new TestModel("ccss", 100);
        testModel.setId(1L);
        mapper.updateTest(testModel);
        System.out.println(mapper.selectTest(1));
    }

    @Test
    public void deleteTest() {
        mapper.deleteTest(1L);
        System.out.println(mapper.selectTest(1));
    }

}
