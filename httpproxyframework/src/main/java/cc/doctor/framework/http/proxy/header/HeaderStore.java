package cc.doctor.framework.http.proxy.header;

import java.util.HashMap;
import java.util.Map;

public abstract class HeaderStore {
    private Map<String, String> headers;

    public abstract Map<String, String> loadHeaders();

    public Map<String, String> getHeaders() {
        if (headers == null) {
            headers = loadHeaders();
        }
        return headers;
    }

    public void setHeaders(Map<String, String> headers) {
        this.headers = headers;
    }

    public Map<String, String> getHeaders(String[] includeHeaders, String[] excludeHeaders) {
        Map<String, String> headers = getHeaders();
        Map<String, String> retHeaders = new HashMap<>();
        if (includeHeaders != null) {
            for (String includeHeader : includeHeaders) {
                if (includeHeader.equals("*")) {
                    retHeaders.putAll(headers);
                    break;
                }
                String value = headers.get(includeHeader);
                if (value != null) {
                    retHeaders.put(includeHeader, value);
                }
            }
        }
        if (excludeHeaders != null) {
            retHeaders.putAll(headers);
            for (String excludeHeader : excludeHeaders) {
                if (excludeHeader.equals("*")) {
                    retHeaders.clear();
                    break;
                }
                retHeaders.remove(excludeHeader);
            }
        }
        return retHeaders;
    }
}
