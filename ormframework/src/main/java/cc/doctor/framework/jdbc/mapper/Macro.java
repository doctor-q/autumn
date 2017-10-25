package cc.doctor.framework.jdbc.mapper;

import java.util.LinkedList;
import java.util.List;
import java.util.Stack;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * a linked list of macro
 */
public class Macro {
    private String name;
    /**
     * 宏模板，如select * from test where $ifNotNull{name,name={{name}}}  $ifNotNull{ids,and id in ($join{ids,,})}被替换成
     * select * from test where ## ##
     */
    private String template;
    private String field;
    private Object value;
    private Macro parentMacro;
    private List<Macro> childrenMacro;
    private boolean leaf;
    private boolean constant;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTemplate() {
        return template;
    }

    public void setTemplate(String template) {
        this.template = template;
    }

    public String getField() {
        return field;
    }

    public void setField(String field) {
        this.field = field;
    }

    public Object getValue() {
        return value;
    }

    public void setValue(Object value) {
        this.value = value;
    }

    public Macro getParentMacro() {
        return parentMacro;
    }

    public void setParentMacro(Macro parentMacro) {
        this.parentMacro = parentMacro;
    }

    public List<Macro> getChildrenMacro() {
        return childrenMacro;
    }

    public void setChildrenMacro(List<Macro> childrenMacro) {
        this.childrenMacro = childrenMacro;
    }

    public boolean isLeaf() {
        return leaf;
    }

    public void setLeaf(boolean leaf) {
        this.leaf = leaf;
    }

    public boolean isConstant() {
        return constant;
    }

    public void setConstant(boolean constant) {
        this.constant = constant;
    }

    public void addChildMacro(Macro macro) {
        if (this.childrenMacro == null) {
            this.childrenMacro = new LinkedList<>();
        }
        this.childrenMacro.add(macro);
    }

    private static Pattern macroPattern = Pattern.compile("((\\$.+?)\\{(.+?),(.+)})");   //$macroName{column,right}
    public static final String MACRO_PLACEHOLDER = "##";

    /**
     * 构建宏树
     */
    public static Macro createMacroTree(String template, Object dataObject, SqlTemplateParser sqlTemplateParser) {
        Macro root = new Macro();
        root.setTemplate(template);
        createMacroTreeInternal(dataObject, sqlTemplateParser, root);
        return root;
    }

    public static void createMacroTreeInternal(Object dataObject, SqlTemplateParser sqlTemplateParser, Macro parent) {
        String parentTemplate = parent.getTemplate();
        List<Macro> childrenMacros = findChildrenMacros(parent, dataObject, sqlTemplateParser);
        if (childrenMacros != null && childrenMacros.size() > 0) {
            for (Macro childrenMacro : childrenMacros) {
                parentTemplate = parentTemplate.replace(childrenMacro.getTemplate(), MACRO_PLACEHOLDER);
                createMacroTreeInternal(dataObject, sqlTemplateParser, childrenMacro);
                parent.addChildMacro(childrenMacro);
            }
            parent.setTemplate(parentTemplate);
        } else {
            parent.setLeaf(true);
        }
    }

    public static List<Macro> findChildrenMacros(Macro parent, Object dataObject, SqlTemplateParser sqlTemplateParser) {
        List<String> macroStrings = new LinkedList<>();
        Stack<Character> characterStack = new Stack<>();
        int dollarStart = -1;
        String template = parent.getTemplate();
        for (int i = 1; i < template.length(); i++) {
            char c = template.charAt(i);
            if (c == '$' && dollarStart < 0) {
                dollarStart = i;
            }
            if (c == '{' && dollarStart > 0) {
                characterStack.push(c);
            }
            if (c == '}' && dollarStart > 0) {
                characterStack.pop();
                if (characterStack.empty()) {
                    macroStrings.add(template.substring(dollarStart, i + 1));
                    dollarStart = -1;
                }
            }
        }
        List<Macro> macros = new LinkedList<>();
        for (String macroString : macroStrings) {
            Matcher matcher = macroPattern.matcher(macroString);
            assert matcher.find();
            Macro macro = new Macro();
            macro.setParentMacro(parent);
            macro.setTemplate(macroString);
            macro.setName(matcher.group(2));
            macro.setField(matcher.group(3));
            macro.setValue(sqlTemplateParser.getData(macro.getField(), dataObject));
            macros.add(macro);
        }
        return macros;
    }

    public static void setMacroTreeTemplate(Macro macro) {
        if (!canSetTemplate(macro)) {
            //后序遍历
            for (Macro child : macro.getChildrenMacro()) {
                setMacroTreeTemplate(child);
            }
            setMacroTreeTemplate(macro);
        } else {
            List<Macro> childrenMacro = macro.getChildrenMacro();
            if (childrenMacro == null) {
                calMacro(macro);
                macro.setConstant(true);
            } else {
                List<String> values = new LinkedList<>();
                for (Macro child : childrenMacro) {
                    values.add(child.getTemplate());
                }
                macro.setTemplate(replaceTemplate(macro.getTemplate(), values));
                calMacro(macro);
                macro.setConstant(true);
            }
        }
    }

    public static void calMacro(Macro macro) {
        for (Macros macros : Macros.values()) {
            if (macros.getName().equals(macro.getName())) {
                String function = macros.function(macro.getValue(), macro.getRight());
                macro.setTemplate(function);
            }
        }
    }

    private static boolean canSetTemplate(Macro macro) {
        List<Macro> childrenMacro = macro.getChildrenMacro();
        if (childrenMacro == null) {
            return true;
        }
        for (Macro child : childrenMacro) {
            if (!child.isConstant()) {
                return false;
            }
        }
        return true;
    }

    private static String replaceTemplate(String template, List<String> replacements) {
        for (String replacement : replacements) {
            template = template.replaceFirst(MACRO_PLACEHOLDER, replacement);
        }
        return template;
    }

    @Override
    public String toString() {
        return "Macro{" +
                "name='" + name + '\'' +
                ", template='" + template + '\'' +
                ", field='" + field + '\'' +
                '}';
    }

    public String getRight() {
        //todo 补偿这次匹配
        Matcher matcher = macroPattern.matcher(template);
        assert matcher.find();
        return matcher.group(4);
    }
}
