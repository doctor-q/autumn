package cc.doctor.framework.jdbc.dao;

/**
 * Created by doctor on 2017/7/12.
 */

import cc.doctor.framework.jdbc.sql.DataModel;
import cc.doctor.framework.jdbc.sql.OffsetLimitBuilder;
import cc.doctor.framework.jdbc.sql.QueryBuilder;
import cc.doctor.framework.jdbc.sql.SortBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.ResultSet;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import static cc.doctor.framework.jdbc.dao.ResultSetHandler.resultSetHandler;
import static cc.doctor.framework.jdbc.sql.DynamicSqlBuilder.DYNAMIC_SQL_BUILDER;

/**
 * Created by doctor on 17-5-22.
 */
public class AbstractDao implements Dao {
    private static final Logger log = LoggerFactory.getLogger(AbstractDao.class);
    private SqlSessionFactory sqlSessionFactory;

    public AbstractDao(SqlSessionFactory sqlSessionFactory) {
        this.sqlSessionFactory = sqlSessionFactory;
    }

    public SqlSessionFactory getSqlSessionFactory() {
        return sqlSessionFactory;
    }

    public void setSqlSessionFactory(SqlSessionFactory sqlSessionFactory) {
        this.sqlSessionFactory = sqlSessionFactory;
    }

    public <D extends DataModel> List<D> selectDataModels(String sql, Class<D> dClass) {
        SqlSession sqlSession = sqlSessionFactory.createSqlSession();
        ResultSet resultSet = sqlSession.select(sql);
        return resultSetHandler.convertDataModels(resultSet, dClass);
    }

    public <D extends DataModel> void insertBatch(Collection<D> dataModels) {
        List<String> insertSqls = new LinkedList<>();
        for (DataModel dataModel : dataModels) {
            String insertSql = DYNAMIC_SQL_BUILDER.insertSql(dataModel);
            insertSqls.add(insertSql);
        }
        log.info("insert batch {}", dataModels.size());
        SqlSession sqlSession = sqlSessionFactory.createSqlSession();
        sqlSession.executeBatch(insertSqls);
    }

    @Override
    public int insert(DataModel dataModel) {
        String sql = DYNAMIC_SQL_BUILDER.insertSql(dataModel);
        SqlSession sqlSession = sqlSessionFactory.createSqlSession();
        sqlSession.insert(sql, false);
        sqlSession.close();
        return 0;
    }

    @Override
    public <D extends DataModel> int updateById(D dataModel) {
        String sql = DYNAMIC_SQL_BUILDER.updateByIdSql(dataModel);
        SqlSession sqlSession = sqlSessionFactory.createSqlSession();
        sqlSession.update(sql);
        sqlSession.close();
        return 0;
    }

    @Override
    public <D extends DataModel> int update(D dataModel, DataModel conditionBean) {
        if (conditionBean == null) {
            return -1;
        }
        String setClause = DYNAMIC_SQL_BUILDER.updateColumnClause(dataModel);
        String whereClause = new QueryBuilder(conditionBean).build();
        if (whereClause == null || whereClause.isEmpty()) {
            return -1;
        }
        String updateSql = String.format("update %s set %s where %s", dataModel.table(), setClause, whereClause);
        SqlSession sqlSession = sqlSessionFactory.createSqlSession();
        sqlSession.update(updateSql);
        sqlSession.close();
        return 0;
    }

    @Override
    public <D extends DataModel> int deleteById(D dataModel) {
        String sql = DYNAMIC_SQL_BUILDER.deleteByIdSql(dataModel);
        SqlSession sqlSession = sqlSessionFactory.createSqlSession();
        sqlSession.update(sql);
        sqlSession.close();
        return 0;
    }

    @Override
    public <D extends DataModel> int delete(D conditionBean) {
        QueryBuilder queryBuilder = new QueryBuilder(conditionBean);
        String clause = queryBuilder.build();
        if (clause != null && !clause.isEmpty()) {
            return -1;
        }
        String sql = String.format("delete from %s where %s", conditionBean.table(), clause);
        SqlSession sqlSession = sqlSessionFactory.createSqlSession();
        sqlSession.delete(sql);
        sqlSession.close();
        return 0;
    }

    @Override
    public <D extends DataModel> List<D> selectList(D conditionBean, SortBuilder sortBuilder, OffsetLimitBuilder offsetLimitBuilder) {
        Class<D> dClass = (Class<D>) conditionBean.getClass();
        return selectList(dClass, conditionBean, sortBuilder, offsetLimitBuilder);
    }

    @Override
    public <D extends DataModel> List<D> selectList(Class<D> dClass, DataModel conditionBean, SortBuilder sortBuilder, OffsetLimitBuilder offsetLimitBuilder) {
        return selectList(dClass, new QueryBuilder(conditionBean), sortBuilder, offsetLimitBuilder);
    }

    @Override
    public <D extends DataModel> List<D> selectList(Class<D> dClass, QueryBuilder queryBuilder, SortBuilder sortBuilder, OffsetLimitBuilder offsetLimitBuilder) {
        String columnClause = DYNAMIC_SQL_BUILDER.selectColumnClause(dClass, true);
        String table = null;
        try {
            table = dClass.newInstance().table();
        } catch (InstantiationException | IllegalAccessException e) {
            throw new RuntimeException("Table not found.");
        }
        String sql = selectSql(table, columnClause, queryBuilder, sortBuilder, offsetLimitBuilder);
        SqlSession sqlSession = sqlSessionFactory.createSqlSession();
        ResultSet resultSet = sqlSession.select(sql);
        return resultSetHandler.convertDataModels(resultSet, dClass);
    }

    @Override
    public List<Map<String, Object>> selectMaps(DataModel conditionBean, SortBuilder sortBuilder, OffsetLimitBuilder offsetLimitBuilder) {
        if (conditionBean == null) {
            return null;
        }
        return selectMaps(conditionBean.table(), new QueryBuilder(conditionBean), sortBuilder, offsetLimitBuilder);
    }

    @Override
    public List<Map<String, Object>> selectMaps(String table, QueryBuilder queryBuilder, SortBuilder sortBuilder, OffsetLimitBuilder offsetLimitBuilder) {
        String sql = selectSql(table, "*", queryBuilder, sortBuilder, offsetLimitBuilder);
        SqlSession sqlSession = sqlSessionFactory.createSqlSession();
        ResultSet resultSet = sqlSession.select(sql);
        return resultSetHandler.convertMaps(resultSet);
    }

    @Override
    public <D extends DataModel> D selectOne(D conditionBean, SortBuilder sortBuilder) {
        Class<D> dClass = (Class<D>) conditionBean.getClass();
        return selectOne(dClass, conditionBean, sortBuilder);
    }

    @Override
    public <D extends DataModel> D selectOne(Class<D> dClass, DataModel conditionBean, SortBuilder sortBuilder) {
        return selectOne(dClass, new QueryBuilder(conditionBean), sortBuilder);
    }

    @Override
    public <D extends DataModel> D selectOne(Class<D> dClass, QueryBuilder queryBuilder, SortBuilder sortBuilder) {
        String columnClause = DYNAMIC_SQL_BUILDER.selectColumnClause(dClass, false);
        String table = null;
        try {
            table = dClass.newInstance().table();
        } catch (InstantiationException | IllegalAccessException e) {
            throw new RuntimeException("Table not found.");
        }
        String sql = selectSql(table, columnClause, queryBuilder, sortBuilder, new OffsetLimitBuilder(1L));
        SqlSession sqlSession = sqlSessionFactory.createSqlSession();
        ResultSet resultSet = sqlSession.select(sql);
        List<D> dataModels = resultSetHandler.convertDataModels(resultSet, dClass);
        if (dataModels.size() > 0) {
            return dataModels.get(0);
        }
        return null;
    }

    private String selectSql(String table, String columnClause, QueryBuilder queryBuilder, SortBuilder sortBuilder, OffsetLimitBuilder offsetLimitBuilder) {
        String sql = String.format("select %s from %s", columnClause, table);
        if (queryBuilder != null) {
            String whereClause = queryBuilder.build();
            if (whereClause != null && !whereClause.isEmpty()) {
                sql += " where " + whereClause;
            }
        }
        String sortClause = null;
        if (sortBuilder != null) {
            sortClause = sortBuilder.build();
            sql += sortClause;
        }
        String pageClause = null;
        if (offsetLimitBuilder != null) {
            pageClause = offsetLimitBuilder.build();
            sql += pageClause;
        }
        return sql;
    }
}