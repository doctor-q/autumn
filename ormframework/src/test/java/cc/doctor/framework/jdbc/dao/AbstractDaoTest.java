package cc.doctor.framework.jdbc.dao;

import cc.doctor.framework.jdbc.mapper.TestModel;
import cc.doctor.framework.jdbc.pool.FakeJdbcPool;
import cc.doctor.framework.jdbc.sql.QueryBuilder;
import cc.doctor.framework.jdbc.sql.QueryMethod;
import org.junit.Before;
import org.junit.Test;

import javax.sql.DataSource;
import java.util.List;
import java.util.Map;

public class AbstractDaoTest {
    private DataSource dataSource;
    private SqlSessionFactory sqlSessionFactory;
    private AbstractDao abstractDao;

    @Before
    public void setUp() {
        dataSource = new FakeJdbcPool(
                "jdbc:mysql://127.0.0.1:3306/brilliant?useUnicode=true&characterEncoding=utf-8", "root", "123456"
        );
        sqlSessionFactory = new SqlSessionFactory(dataSource);
        abstractDao = new AbstractDao(sqlSessionFactory);
    }

    @Test
    public void insert() throws Exception {
        for (int i = 0; i < 10; i++) {
            TestModel testModel = new TestModel(null, 10 + i);
            abstractDao.insert(testModel);
        }
    }

    @Test
    public void updateById() throws Exception {
        TestModel testModel = new TestModel("cccc", 1000);
        testModel.setId(41L);
        abstractDao.updateById(testModel);
    }

    @Test
    public void update() throws Exception {
        TestModel testModel = new TestModel("cccc", 1000);
        TestModel update = new TestModel("dddd", 100);
        abstractDao.update(update, testModel);
    }

    @Test
    public void deleteById() throws Exception {
        TestModel testModel = new TestModel();
        testModel.setId(42L);
        abstractDao.deleteById(testModel);
    }

    @Test
    public void delete() throws Exception {
    }

    @Test
    public void selectList() throws Exception {
        TestModel testModel = new TestModel("cccc", 1000);
        List<TestModel> testModels = abstractDao.selectList(testModel, null, null);
        for (TestModel model : testModels) {
            System.out.println(model);
        }
    }

    @Test
    public void selectListCondition() {
        QueryBuilder queryBuilder = new QueryBuilder();
        queryBuilder.addQuery(new QueryBuilder.QueryEntity("like", "name", new QueryMethod.LikeQuery("name")))
        .addQuery(new QueryBuilder.QueryEntity("range", "age", new QueryMethod.RangeQuery().left(10).right(15)));
        List<TestModel> testModels = abstractDao.selectList(TestModel.class, queryBuilder, null, null);
        for (TestModel testModel : testModels) {
            System.out.println(testModel);
        }
    }

    @Test
    public void selectMaps() throws Exception {
        TestModel testModel = new TestModel("cccc", 1000);
        List<Map<String, Object>> testModels = abstractDao.selectMaps(testModel, null, null);
        for (Map<String, Object> model : testModels) {
            System.out.println(model);
        }
    }

    @Test
    public void selectOne() throws Exception {
        TestModel testModel = new TestModel("cccc", 1000);
        TestModel testModels = abstractDao.selectOne(TestModel.class, testModel, null);
        System.out.println(testModels);
    }

}