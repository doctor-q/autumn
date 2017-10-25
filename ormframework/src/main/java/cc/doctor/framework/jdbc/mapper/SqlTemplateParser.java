package cc.doctor.framework.jdbc.mapper;

import cc.doctor.framework.utils.ReflectUtils;

import java.lang.reflect.Field;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 动态sql模板解析
 */
public class SqlTemplateParser {
    private Map<String, Object> pathDataCache = new HashMap<>();
    public SqlTemplateParser() {
    }

    /**
     * 1. 解析宏树
     * 2. 替换值
     */
    public String parse(String template, Object dataObject) {
        if (dataObject == null) {
            return template;
        }
        Macro macroTree = Macro.createMacroTree(template, dataObject, this);
        Macro.setMacroTreeTemplate(macroTree);
        template = macroTree.getTemplate();
        List<String> paths = extractDataPaths(template);
        List<Object> datas = new LinkedList<>();
        for (String path : paths) {
            datas.add(getData(path, dataObject));
        }
        return replace(template, paths, datas);
    }

    /**
     * 抽取数据路径{{path}}
     */
    private List<String> extractDataPaths(String sqlTemplate) {
        List<String> dataPaths = new LinkedList<>();
        Pattern pattern = Pattern.compile("(\\{\\{(.+?)}})");
        Matcher matcher = pattern.matcher(sqlTemplate);
        while (matcher.find()) {
            String result = matcher.group(2);
            dataPaths.add(result);
        }
        return dataPaths;
    }

    public Object getData(String path, Object datas) {
        if (path == null || datas == null) {
            return null;
        }
        if (pathDataCache.get(path) != null) {
            return pathDataCache.get(path);
        }
        String[] split = path.split("\\.");
        for (String s : split) {
            if (datas instanceof Map) {
                datas = ((Map)datas).get(s);
            } else {
                Map<String, Field> attrNameFields = ReflectUtils.getObjectAttrNameFields(datas.getClass());
                if (!attrNameFields.containsKey(s)) {
                    return datas;
                } else {
                    datas = ReflectUtils.get(s, datas);
                    if (datas == null) {
                        return null;
                    }
                }
            }
        }
        pathDataCache.put(path, datas);
        return datas;
    }

    private String replace(String template, List<String> values, List<Object> datas) {
        for (int i = 0; i < values.size(); i++) {
            template = template.replaceFirst(escapeExprSpecialWord(String.format("{{%s}}", values.get(i))), datas.get(i).toString());
        }
        return template;
    }

    public static String escapeExprSpecialWord(String keyword) {
        if (keyword != null && !keyword.isEmpty()) {
            String[] fbsArr = { "\\", "$", "(", ")", "*", "+", ".", "[", "]", "?", "^", "{", "}", "|" };
            for (String key : fbsArr) {
                if (keyword.contains(key)) {
                    keyword = keyword.replace(key, "\\" + key);
                }
            }
        }
        return keyword;
    }
}
