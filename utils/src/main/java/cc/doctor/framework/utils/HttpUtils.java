package cc.doctor.framework.utils;

import cc.doctor.dsp.handler.file.FileLimitation;
import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.HttpMessage;
import org.apache.http.ParseException;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.charset.UnsupportedCharsetException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
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

    public static String parseHttpResponse(CloseableHttpResponse response) {
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

    public static CloseableHttpResponse post(String url, Map<String, String> params) {
        return post(url, null, params, null);
    }

    public static CloseableHttpResponse post(String url, String params) {
        HttpPost httpPost = new HttpPost(url);
        try {
            StringEntity uefEntity = new StringEntity(params, "UTF-8");
            httpPost.setEntity(uefEntity);
            return execute(httpPost);
        } catch (UnsupportedCharsetException e) {
            log.error("", e);
        }
        return null;
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

    public static CloseableHttpResponse get(String url, Map<String, String> params, Map<String, String> headers) {
        List<BasicNameValuePair> formParams = new ArrayList<>();
        for (String name : params.keySet()) {
            formParams.add(new BasicNameValuePair(name, params.get(name)));
        }
        try {
            UrlEncodedFormEntity uefEntity = new UrlEncodedFormEntity(formParams, Charset.defaultCharset());
            String param = EntityUtils.toString(uefEntity);
            HttpGet httpGet = new HttpGet(url + "?" + param);
            setHeaders(httpGet, headers);
            return execute(httpGet);
        } catch (ParseException | IOException e) {
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

    public static CloseableHttpResponse postJson(String url, String json, Map<String, String> headers) {
        HttpPost httpPost = new HttpPost(url);
        setHeaders(httpPost, headers);
        httpPost.addHeader(HTTP.CONTENT_TYPE, "application/json");
        StringEntity stringEntity = new StringEntity(json, "UTF-8");    //中文乱码问题解决
        httpPost.setEntity(stringEntity);
        return execute(httpPost);
    }

    /**
     * 上传文件
     *
     * @param serverUrl       服务器地址
     * @param localFilePath   本地文件路径
     * @param serverFieldName 服务端字段名
     * @param getParams       get参数
     * @param postParams      附加参数
     */
    public static CloseableHttpResponse multipart(String serverUrl, String localFilePath, String serverFieldName, Map<String, String> getParams, Map<String, String> postParams, FileLimitation fileLimitation) throws InvalidException {
        // do file limitation
        if (fileLimitation != null) {
            if (!fileLimitation.validate(localFilePath)) {
                throw new InvalidException("File invalid");
            }
        }
        //get params
        List<BasicNameValuePair> formParams = new ArrayList<>();
        if (getParams != null) {
            for (String name : getParams.keySet()) {
                formParams.add(new BasicNameValuePair(name, getParams.get(name)));
            }
        }
        UrlEncodedFormEntity uefEntity = new UrlEncodedFormEntity(formParams, Charset.defaultCharset());
        String param = null;
        try {
            param = EntityUtils.toString(uefEntity);
        } catch (IOException e) {
            log.error("", e);
        }
        if (param != null && !param.trim().isEmpty()) {
            serverUrl = serverUrl + "?" + param;
        }

        HttpPost httppost = new HttpPost(serverUrl);
        FileBody binFileBody = new FileBody(new File(localFilePath));
        MultipartEntityBuilder multipartEntityBuilder = MultipartEntityBuilder.create();
        // add the file params
        multipartEntityBuilder.addPart(serverFieldName, binFileBody);
        // 设置上传的其他参数
        if (postParams != null && postParams.size() > 0) {
            for (String key : postParams.keySet()) {
                multipartEntityBuilder.addPart(key, new StringBody(postParams.get(key), ContentType.TEXT_PLAIN));
            }
        }

        HttpEntity reqEntity = multipartEntityBuilder.build();
        httppost.setEntity(reqEntity);
        return execute(httppost);
    }

    /**
     * post 请求
     *
     * @param url          请求url
     * @param getParamMap  get参数，拼接到url
     * @param postParamMap post参数，作为body
     */
    public static CloseableHttpResponse post(String url, Map<String, String> getParamMap,
                                             Map<String, String> postParamMap, Map<String, String> headers) {
        url = urlWithGetParams(url, getParamMap);
        //post params
        List<BasicNameValuePair> formParams = new ArrayList<>();
        HttpPost httpPost = new HttpPost(url);
        for (String name : postParamMap.keySet()) {
            formParams.add(new BasicNameValuePair(name, postParamMap.get(name)));
        }
        try {
            UrlEncodedFormEntity uefEntity = new UrlEncodedFormEntity(formParams, "UTF-8");
            httpPost.setEntity(uefEntity);
            setHeaders(httpPost, headers);
            return execute(httpPost);
        } catch (IOException e) {
            log.error("", e);
        }
        return null;
    }

    public static CloseableHttpResponse postJson(String url, Map<String, String> getParamMap,
                                                 String json, Map<String, String> headers) {
        url = urlWithGetParams(url, getParamMap);
        return postJson(url, json, headers);
    }

    private static String urlWithGetParams(String url, Map<String, String> getParamMap) {
        if (getParamMap == null || getParamMap.size() == 0) {
            return url;
        }
        //get params
        List<BasicNameValuePair> formParams = new ArrayList<>();
        for (String name : getParamMap.keySet()) {
            formParams.add(new BasicNameValuePair(name, getParamMap.get(name)));
        }
        UrlEncodedFormEntity uefEntity = new UrlEncodedFormEntity(formParams, Charset.defaultCharset());
        String param = null;
        try {
            param = EntityUtils.toString(uefEntity);
        } catch (IOException e) {
            log.error("", e);
        }
        if (param != null && !param.trim().isEmpty()) {
            url = url + "?" + param;
        }
        return url;
    }
}
