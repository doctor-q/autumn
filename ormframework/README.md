ORM
##模型SQL
只支持单表查询，复杂查询使用SQL模板
- model定义
```
public abstract class DataModel {
    public String generateId() {
        return "id";
    }

    public abstract String table();
}
```
继承DataModel
- 注解
    - insert
        ``` 
            @InsertIgnore
            private Long id;
            @InsertNotSkipNull
            private String name;
        ```
    - update
        ``` 
            @UpdateIgnore
            private Long id;
            @UpdateNotSkipNull
            private String name;
        ```
    - select
        ```
            @SelectIgnore
            private Long id;
            @Column("username")
            @As("name")
            private String name;
            @ListIgnore
            private String desc; 
        ```
    - Query
        ```
            @Query
            private String name; 
        ```
    - 数据转换
    ``` 
    @DateFormat
    private Date date;
    @Encrpt(secretKey="123456")
    private String password;
    @Decrypt(secretKey="123456")
    private String password;
    ```
##QueryBuilder
query类型
```
equal
in
range
like 
```
##SQL模板
- 定义Mapper
    ```
    @Mapper
    public interface TestMapper {
    } 
    ```
- 字段获取
    ``` 
    {{field}} 
    ```
    获取字段名为field的值
    ```
    @Param("alias")
    ```
    参数的别名，多个参数时候需要根据参数的别名区分
- 定义操作
    - select
        ```
            @Select(
                    sql = "select * from test where id={{id}}",
                    resultType = TestModel.class
            )
            List<TestModel> selectTest(@Param("id") long id);
        ```
    - insert 
        ```
            @Insert(
                    sql = "insert into test (name, age) values ('{{name}}', {{age}})"
            )
            @ReturnPK
            long insertTest(TestModel testModel); 
        ```
        返回主键 ```    @ReturnPk   ```
    
    - update
        ``` 
            @Update(
                    sql = "update test set name='${name}', age={{age}} where id={{id}}"
            )
            void updateTest(TestModel testModel);
        ```
    - delete
        ``` 
            @Delete(
                    sql = "delete from test where id={{id}}"
            )
            void deleteTest(Long id);
        ```
- 宏的使用
``` 
select * from test 
where $ifNotNull{ids, id in ($join{ids, ,})} 
$ifNotNull{cc.name, and name like '{{cc.name}}%'}
```
二元宏：``` $macroName{path, right}```  
  path是获取数据的路径  
  right是左指，可以是常量，也可以是带有宏的字符串
