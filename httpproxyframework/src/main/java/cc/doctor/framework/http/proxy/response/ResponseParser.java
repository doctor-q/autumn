package cc.doctor.framework.http.proxy.response;

import cc.doctor.dsp.handler.HttpUtils;
import org.apache.http.StatusLine;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public abstract class ResponseParser {
    private static final Logger log = LoggerFactory.getLogger(ResponseParser.class);

    public Object parseHttpResponse(Class retType, CloseableHttpResponse httpResponse) {
        StatusLine statusLine = httpResponse.getStatusLine();
        if (statusLine.getStatusCode() == 200) {
            String response = HttpUtils.parseHttpResponse(httpResponse);
            return parseResponse(retType, response);
        } else {
            log.error("Http error[{}] message[{}]", statusLine.getStatusCode(), statusLine.getReasonPhrase());
        }
        return null;
    }

    protected abstract Object parseResponse(Class retType, String response);
}
