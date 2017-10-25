package cc.doctor.framework.web.servlet.meta;

import java.util.Map;

/**
 * Created by doctor on 2017/7/21.
 */
public class HttpMetaParameters {
    private String json;
    private Map<String, String> params;
    private Map<String, Object> attributes;

    public String getJson() {
        return json;
    }

    public void setJson(String json) {
        this.json = json;
    }

    public Map<String, String> getParams() {
        return params;
    }

    public void setParams(Map<String, String> params) {
        this.params = params;
    }

    public Map<String, Object> getAttributes() {
        return attributes;
    }

    public void setAttributes(Map<String, Object> attributes) {
        this.attributes = attributes;
    }

    public String getParamString(String name) {
        Object value = null;
        if (params != null) {
            value = params.get(name);
        }
        if (value == null && attributes != null) {
            value = attributes.get(name);
        }
        return value == null ? null : value.toString();
    }
}
