package cc.doctor.framework.http.proxy;

import cc.doctor.framework.utils.HttpUtils;
import org.apache.http.Header;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;

public class HttpClientPool {
    private static final Logger log = LoggerFactory.getLogger(HttpUtils.class);

    private static PoolingHttpClientConnectionManager poolingHttpClientConnectionManager = new PoolingHttpClientConnectionManager();

    public static CloseableHttpClient getHttpClient() {
        return HttpClients.custom().setConnectionManager(poolingHttpClientConnectionManager).build();
    }

    public static Map<String, String> getResponseHeaders(CloseableHttpResponse response) {
        Map<String, String> headers = new HashMap<>();
        Header[] allHeaders = response.getAllHeaders();
        for (Header allHeader : allHeaders) {
            headers.put(allHeader.getName(), allHeader.getValue());
        }
        return headers;
    }
}
