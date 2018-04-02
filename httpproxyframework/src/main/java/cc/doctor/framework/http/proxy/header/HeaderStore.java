package cc.doctor.framework.http.proxy.header;

import java.util.Map;

public abstract class HeaderStore {
    private Map<String, String> headers;

    public abstract void loadHeaders(Map<String , String> responseHeaders);

    public Map<String, String> getHeaders() {
        return headers;
    }

    public void setHeaders(Map<String, String> headers) {
        this.headers = headers;
    }
}
