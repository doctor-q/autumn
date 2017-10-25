package cc.doctor.framework.web.servlet.meta;

import javax.servlet.http.Cookie;
import java.util.Map;

/**
 * Created by doctor on 2017/7/15.
 */
public class HttpMetaData {
    //    http request path
    private String path;
    //    http head
    private Map<String, String> headers;
    // method
    private String method;
    private Cookie[] cookies;

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public Map<String, String> getHeaders() {
        return headers;
    }

    public void setHeaders(Map<String, String> headers) {
        this.headers = headers;
    }

    public String getMethod() {
        return method;
    }

    public void setMethod(String method) {
        this.method = method;
    }

    public Cookie[] getCookies() {
        return cookies;
    }

    public void setCookies(Cookie[] cookies) {
        this.cookies = cookies;
    }
}
