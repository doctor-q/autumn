package cc.doctor.framework.utils;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.ParseException;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.conn.ssl.TrustSelfSignedStrategy;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicHeader;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HTTP;
import org.apache.http.ssl.SSLContexts;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.net.ssl.SSLContext;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.security.KeyManagementException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by doctor on 2017/7/15.
 */
public class HttpUtils {
    private static final Logger log = LoggerFactory.getLogger(HttpUtils.class);

    private static HttpResponse execute(CloseableHttpClient httpClient, HttpRequestBase httpRequestBase) {
        try {
            return httpClient.execute(httpRequestBase);
        } catch (Exception e) {
            log.error("", e);
        } finally {
            try {
                httpClient.close();
            } catch (IOException e) {
                log.error("", e);
            }
        }
        return null;
    }

    public static String parseHttpResponse(HttpResponse response) {
        HttpEntity entity = response.getEntity();
        if (entity != null) {
            try {
                return EntityUtils.toString(entity);
            } catch (IOException e) {
                log.error("", e);
            }
        }
        return null;
    }

    public static HttpResponse ssl(String keyStore, String url) {
        CloseableHttpClient httpclient = null;
        try {
            KeyStore trustStore = KeyStore.getInstance(KeyStore.getDefaultType());
            FileInputStream fileInputStream = new FileInputStream(new File(keyStore));
            trustStore.load(fileInputStream, "123456".toCharArray());
            // 相信自己的CA和所有自签名的证书
            SSLContext sslcontext = SSLContexts.custom().loadTrustMaterial(trustStore, new TrustSelfSignedStrategy()).build();
            // 只允许使用TLSv1协议
            SSLConnectionSocketFactory sslsf = new SSLConnectionSocketFactory(sslcontext, new String[]{"TLSv1"}, null,
                    SSLConnectionSocketFactory.BROWSER_COMPATIBLE_HOSTNAME_VERIFIER);
            httpclient = HttpClients.custom().setSSLSocketFactory(sslsf).build();
            // 创建http请求(get方式)
            HttpGet httpget = new HttpGet(url);
            return execute(httpclient, httpget);
        } catch (ParseException | IOException | KeyManagementException | KeyStoreException | NoSuchAlgorithmException | CertificateException e) {
            log.error("", e);
        }
        return null;
    }

    public static HttpResponse post(String url, Map<String, String> params) {
        CloseableHttpClient httpclient = HttpClients.createDefault();
        HttpPost httpPost = new HttpPost(url);
        List<BasicNameValuePair> formParams = new ArrayList<>();
        for (String name : params.keySet()) {
            formParams.add(new BasicNameValuePair(name, params.get(name)));
        }
        UrlEncodedFormEntity uefEntity;
        try {
            uefEntity = new UrlEncodedFormEntity(formParams, "UTF-8");
            httpPost.setEntity(uefEntity);
            return execute(httpclient, httpPost);
        } catch (IOException e) {
            log.error("", e);
        }
        return null;
    }

    public static HttpResponse get(String url) {
        CloseableHttpClient httpClient = HttpClients.createDefault();
        try {
            HttpGet httpGet = new HttpGet(url);
            return execute(httpClient, httpGet);
        } catch (ParseException e) {
            log.error("", e);
        }
        return null;
    }

    public static HttpResponse postJson(String url, String json) {
        try {
            CloseableHttpClient httpClient = HttpClients.createDefault();
            HttpPost httpPost = new HttpPost(url);
            httpPost.addHeader(HTTP.CONTENT_TYPE, "application/json; charset=utf-8");
            StringEntity stringEntity = new StringEntity(json);
            stringEntity.setContentEncoding(new BasicHeader(HTTP.CONTENT_TYPE, "application/json; charset=utf-8"));
            httpPost.setEntity(stringEntity);
            return execute(httpClient, httpPost);
        } catch (IOException e) {
            log.error("", e);
        }
        return null;
    }
}
