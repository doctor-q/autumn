package cc.doctor.framework.web.servlet.meta;

import javax.servlet.http.Cookie;
import java.util.List;
import java.util.Map;

/**
 * Created by doctor on 2017/7/21.
 */
public class HttpMetadata {
    //    http request path
    private String path;
    // http method, get, post
    private String method;
    //    http head
    private Map<String, String> headers;
    private String json;
    private Map<String, String> params;
    private Map<String, Object> attributes;
    private List<Cookie> cookies;

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getMethod() {
        return method;
    }

    public void setMethod(String method) {
        this.method = method;
    }

    public Map<String, String> getHeaders() {
        return headers;
    }

    public void setHeaders(Map<String, String> headers) {
        this.headers = headers;
    }

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

    public List<Cookie> getCookies() {
        return cookies;
    }

    public void setCookies(List<Cookie> cookies) {
        this.cookies = cookies;
    }

    public String getParam(String name) {
        if (name == null) {
            return null;
        }
        return params.get(name);
    }

    public String getAttribute(String name) {
        if (name == null) {
            return null;
        }
        Object attr = attributes.get(name);
        return attr == null ? null : attr.toString();
    }

    public String getCookie(String name) {
        if (name == null) {
            return null;
        }
        for (Cookie cookie : cookies) {
            if (name.equals(cookie.getName())) {
                return cookie.getValue();
            }
        }
        return null;
    }

    public String getHeader(String name) {
        if (name == null) {
            return null;
        }
        Object header = headers.get(name);
        return header == null ? null : header.toString();
    }
}
