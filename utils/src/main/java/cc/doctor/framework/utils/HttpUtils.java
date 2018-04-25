package cc.doctor.framework.utils;

import org.apache.http.Header;
import org.apache.http.HttpMessage;
import org.apache.http.ParseException;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by doctor on 2017/7/15.
 */
public class HttpUtils {
    private static final Logger log = LoggerFactory.getLogger(HttpUtils.class);

    private static PoolingHttpClientConnectionManager poolingHttpClientConnectionManager = new PoolingHttpClientConnectionManager();

    public static CloseableHttpClient getHttpClient() {
        return HttpClients.custom().setConnectionManager(poolingHttpClientConnectionManager).build();
    }

    private static CloseableHttpResponse execute(HttpRequestBase httpRequestBase) {
        try {
            return getHttpClient().execute(httpRequestBase);
        } catch (Exception e) {
            log.error("", e);
        }
        return null;
    }

    public static Map<String, String> getResponseHeaders(CloseableHttpResponse response) {
        Map<String, String> headers = new HashMap<>();
        Header[] allHeaders = response.getAllHeaders();
        for (Header allHeader : allHeaders) {
            headers.put(allHeader.getName(), allHeader.getValue());
        }
        return headers;
    }


    public static CloseableHttpResponse get(String url) {
        try {
            HttpGet httpGet = new HttpGet(url);
            return execute(httpGet);
        } catch (ParseException e) {
            log.error("", e);
        }
        return null;
    }

    private static void setHeaders(HttpMessage httpMessage, Map<String, String> headers) {
        if (headers != null) {
            for (Map.Entry<String, String> header : headers.entrySet()) {
                httpMessage.setHeader(header.getKey(), header.getValue());
            }
        }
    }

}
