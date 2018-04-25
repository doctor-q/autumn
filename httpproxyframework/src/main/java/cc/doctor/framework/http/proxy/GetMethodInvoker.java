package cc.doctor.framework.http.proxy;

import org.apache.http.ParseException;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;


public class GetMethodInvoker extends MethodInvoker {
    @Override
    public CloseableHttpResponse invoke() {
        return get(generateUrl());
    }

    public CloseableHttpResponse get(String url) {
        try {
            HttpGet httpGet = new HttpGet(url);
            bindHeaders(httpGet);
            return execute(httpGet);
        } catch (ParseException e) {
            log.error("", e);
        }
        return null;
    }
}
