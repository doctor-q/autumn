package cc.doctor.framework.http.proxy;

import cc.doctor.framework.http.proxy.request.ParamTuple;
import com.alibaba.fastjson.JSONObject;
import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.protocol.HTTP;

import java.io.File;
import java.util.Map;

public class PostMethodInvoker extends MethodInvoker {
    private ContentType contentType;
    private boolean multipart;
    private Map<String, String> parts;

    public ContentType getContentType() {
        return contentType;
    }

    public void setContentType(ContentType contentType) {
        this.contentType = contentType;
    }

    public boolean isMultipart() {
        return multipart;
    }

    public void setMultipart(boolean multipart) {
        this.multipart = multipart;
    }

    public Map<String, String> getParts() {
        return parts;
    }

    public void setParts(Map<String, String> parts) {
        this.parts = parts;
    }

    @Override
    CloseableHttpResponse invoke() {
        ParamTuple paramTuple = getParamTuple();
        if (multipart) {
            return multipart(generateUrl(), parts, paramTuple.getPostParam());
        }
        if (contentType.equals(ContentType.APPLICATION_JSON)) {
            return post(generateUrl(), paramTuple.joinPostParam());
        } else {
            return post(generateUrl(), JSONObject.toJSONString(paramTuple.getPostParam()));
        }
    }


    public CloseableHttpResponse post(String url, String params) {
        HttpPost httpPost = new HttpPost(url);
        bindHeaders(httpPost);
        httpPost.addHeader(HTTP.CONTENT_TYPE, contentType.getMimeType());
        StringEntity stringEntity = new StringEntity(params, "UTF-8");    //中文乱码问题解决
        httpPost.setEntity(stringEntity);
        return execute(httpPost);
    }

    /**
     * 上传文件
     *
     * @param serverUrl 服务器地址
     * @param params    附加参数
     */
    public CloseableHttpResponse multipart(String serverUrl, Map<String, String> parts, Map<String, String> params) {

        HttpPost httppost = new HttpPost(serverUrl);
        bindHeaders(httppost);
        MultipartEntityBuilder multipartEntityBuilder = MultipartEntityBuilder.create();
        for (Map.Entry<String, String> entry : parts.entrySet()) {
            FileBody binFileBody = new FileBody(new File(entry.getValue()));
            multipartEntityBuilder.addPart(entry.getKey(), binFileBody);
        }
        // 设置上传的其他参数
        if (params != null && params.size() > 0) {
            for (String key : params.keySet()) {
                multipartEntityBuilder.addPart(key, new StringBody(params.get(key), ContentType.TEXT_PLAIN));
            }
        }

        HttpEntity reqEntity = multipartEntityBuilder.build();
        httppost.setEntity(reqEntity);
        return execute(httppost);
    }

}
