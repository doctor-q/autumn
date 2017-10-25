package cc.doctor.framework.jdbc.dao;

import cc.doctor.framework.jdbc.sql.DataModel;
import cc.doctor.framework.jdbc.sql.OffsetLimitBuilder;
import cc.doctor.framework.jdbc.sql.QueryBuilder;
import cc.doctor.framework.jdbc.sql.SortBuilder;

import java.util.List;
import java.util.Map;

/**
 * Created by doctor on 2017/8/8.
 */
public interface Dao {
    <D extends DataModel> int insert(D dataModel);

    /**
     * update by id,update a data model
     * @param dataModel contains id
     */
    <D extends DataModel> int updateById(D dataModel);

    /**
     * update by where condition, update one or more data model
     * @param dataModel a data model
     * @param conditionBean where condition bean
     */
    <D extends DataModel> int update(D dataModel, DataModel conditionBean);

    /**
     * delete by id, delete one data model
     * @param dataModel contains id
     */
    <D extends DataModel> int deleteById(D dataModel);

    /**
     * delete by condition
     */
    <D extends DataModel>int delete(D conditionBean);

    /**
     * select a list of data
     * @param conditionBean as both result type and query condition
     */
    <D extends DataModel> List<D> selectList(D conditionBean, SortBuilder sortBuilder, OffsetLimitBuilder offsetLimitBuilder);


    /**
     * select a list of data
     * @param dClass a data model class as result type
     * @param conditionBean as query condition
     * @param sortBuilder sort condition
     * @param offsetLimitBuilder paging condition
     */
    <D extends DataModel> List<D> selectList(Class<D> dClass, DataModel conditionBean, SortBuilder sortBuilder, OffsetLimitBuilder offsetLimitBuilder);

    <D extends DataModel> List<D> selectList(Class<D> dClass, QueryBuilder queryBuilder, SortBuilder sortBuilder, OffsetLimitBuilder offsetLimitBuilder);

    /**
     * select a set of map
     */
    List<Map<String, Object>> selectMaps(DataModel conditionBean, SortBuilder sortBuilder, OffsetLimitBuilder offsetLimitBuilder);

    List<Map<String, Object>> selectMaps(String table, QueryBuilder queryBuilder, SortBuilder sortBuilder, OffsetLimitBuilder offsetLimitBuilder);

    /**
     * select one data model
     */
    <D extends DataModel> D selectOne(D conditionBean, SortBuilder sortBuilder);

    /**
     * select one data model
     */
    <D extends DataModel> D selectOne(Class<D> dClass, DataModel conditionBean, SortBuilder sortBuilder);

    <D extends DataModel> D selectOne(Class<D> dClass, QueryBuilder queryBuilder, SortBuilder sortBuilder);
}
