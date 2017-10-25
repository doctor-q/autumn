package cc.doctor.framework.jdbc.mapper;

import org.junit.Before;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.*;

public class SqlTemplateParserTest {
    private TestModel testModel;
    private Map<String, Object> map;
    private SqlTemplateParser sqlTemplateParser;

    @Before
    public void setup() {
        sqlTemplateParser = new SqlTemplateParser();
        testModel = new TestModel("cc", 100);
        map = new HashMap<>();
        map.put("cc", testModel);
        map.put("number", 10);
        map.put("ids", new Integer[] {1,2,3,4,5});
    }
    @Test
    public void parse() throws Exception {
        String sql = sqlTemplateParser.parse("select * from test where $ifNotNull{ids, id in ($join{ids, ,})} $ifNotNull{cc.name, and name like '{{cc.name}}}%'", map);
        System.out.println(sql);
    }

    @Test
    public void getDataFromSingle() throws Exception {
        Object id = sqlTemplateParser.getData("id", 1);
        System.out.println(id);
    }

    @Test
    public void getDataFromMap() throws Exception {
        Object number = sqlTemplateParser.getData("number", map);
        System.out.println(number);
    }

    @Test
    public void getDataFromEntity() throws Exception {
        Object name = sqlTemplateParser.getData("name", testModel);
        System.out.println(name);
    }

    @Test
    public void getDataFromMapEntity() throws Exception {
        Object data = sqlTemplateParser.getData("cc.name", map);
        System.out.println(data);
    }
}