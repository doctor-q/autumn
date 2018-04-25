package cc.doctor.framework.http.proxy;

import cc.doctor.framework.http.proxy.request.ParamTuple;
import org.apache.http.HttpEntity;
import org.apache.http.HttpMessage;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.Map;

public abstract class MethodInvoker {
    protected static final Logger log = LoggerFactory.getLogger(MethodInvoker.class);

    private String url;
    private String subUrl;
    private ParamTuple paramTuple;
    private Map<String, String> headers;

    abstract CloseableHttpResponse invoke();

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getSubUrl() {
        return subUrl;
    }

    public void setSubUrl(String subUrl) {
        this.subUrl = subUrl;
    }

    public ParamTuple getParamTuple() {
        return paramTuple;
    }

    public void setParamTuple(ParamTuple paramTuple) {
        this.paramTuple = paramTuple;
    }

    public Map<String, String> getHeaders() {
        return headers;
    }

    public void setHeaders(Map<String, String> headers) {
        this.headers = headers;
    }

    public void bindHeaders(HttpMessage httpMessage) {
        for (Map.Entry<String, String> entry : headers.entrySet()) {
            httpMessage.addHeader(entry.getKey(), entry.getValue());
        }
    }

    public CloseableHttpResponse execute(HttpRequestBase httpRequestBase) {
        try {
            return HttpClientPool.getHttpClient().execute(httpRequestBase);
        } catch (Exception e) {
            log.error("", e);
        }
        return null;
    }

    public String generateUrl() {
        return String.format("%s%s?%s", url, subUrl, paramTuple.joinGetParam());
    }
}
