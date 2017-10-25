package cc.doctor.framework.jdbc.mapper;

import org.junit.Before;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

public class MacroTest {
    private SqlTemplateParser sqlTemplateParser;
    Macro macroTree;
    Map<String, Object> map;

    @Before
    public void setup() {
        sqlTemplateParser = new SqlTemplateParser();
        map = new HashMap<>();
        map.put("ids", new Integer[] {1,2,3,4,5});
        map.put("name", "cc");
    }

    @Test
    public void createMacroTree() throws Exception {
        String template = "select * from test where $ifNotNull{ids, id in ($join{ids, ,})} $ifNotNull{name, and name like '{{name}}}%'";
        macroTree = Macro.createMacroTree(template, map, sqlTemplateParser);
        System.out.println(macroTree);
    }

    @Test
    public void setMacroTreeTemplate() throws Exception {
        createMacroTree();
        Macro.setMacroTreeTemplate(macroTree);
        System.out.println(macroTree.getTemplate());
    }

}