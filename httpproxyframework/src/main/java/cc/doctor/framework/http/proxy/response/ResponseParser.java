package cc.doctor.framework.http.proxy.response;

import com.alibaba.fastjson.JSONObject;
import org.apache.http.HttpEntity;
import org.apache.http.StatusLine;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

public abstract class ResponseParser {
    private static final Logger log = LoggerFactory.getLogger(ResponseParser.class);

    public Object parseHttpResponse(Class retType, CloseableHttpResponse httpResponse) {
        StatusLine statusLine = httpResponse.getStatusLine();
        if (statusLine.getStatusCode() == 200) {
            String response = parseHttpResponse(httpResponse);
            return parseResponse(retType, response);
        } else {
            log.error("Http error[{}] message[{}]", statusLine.getStatusCode(), statusLine.getReasonPhrase());
        }
        return null;
    }


    public String parseHttpResponse(CloseableHttpResponse response) {
        HttpEntity entity = response.getEntity();
        if (entity != null) {
            try {
                return EntityUtils.toString(entity);
            } catch (IOException e) {
                log.error("", e);
            } finally {
                try {
                    response.close();
                } catch (IOException e) {
                    log.error("", e);
                }
            }
        }
        return null;
    }

    protected abstract Object parseResponse(Class retType, String response);

    static class DefaultResponseParser extends ResponseParser {
        private static DefaultResponseParser defaultResponseParser;

        public static DefaultResponseParser getInstance() {
            if (defaultResponseParser == null) {
                defaultResponseParser = new DefaultResponseParser();
            }
            return defaultResponseParser;
        }

        private DefaultResponseParser() {
        }

        @Override
        protected Object parseResponse(Class retType, String response) {
            return JSONObject.parseObject(response, retType);
        }
    }

    public static ResponseParser defaultResponseParser() {
        return DefaultResponseParser.getInstance();
    }
}
