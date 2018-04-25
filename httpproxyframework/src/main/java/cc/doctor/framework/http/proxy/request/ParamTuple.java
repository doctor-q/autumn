package cc.doctor.framework.http.proxy.request;

import cc.doctor.framework.utils.CollectionUtils;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class ParamTuple {
    /**
     * Get 参数，追加到url后面
     */
    private Map<String, String> getParam;
    /**
     * post参数，作为请求body封装
     */
    private Map<String, String> postParam;

    /**
     * 使用的头部
     */
    private Map<String, String> headers;

    public Map<String, String> getGetParam() {
        return getParam;
    }

    public void setGetParam(Map<String, String> getParam) {
        this.getParam = getParam;
    }

    public Map<String, String> getPostParam() {
        return postParam;
    }

    public void setPostParam(Map<String, String> postParam) {
        this.postParam = postParam;
    }

    public Map<String, String> getHeaders() {
        return headers;
    }

    public void setHeaders(Map<String, String> headers) {
        this.headers = headers;
    }

    public String joinGetParam() {
        List<String> keyValues = new LinkedList<>();
        for (Map.Entry<String, String> entry : getParam.entrySet()) {
            keyValues.add(entry.getKey() + "=" + entry.getValue());
        }
        return CollectionUtils.join(keyValues, "&");
    }

    public String joinPostParam() {
        List<String> keyValues = new LinkedList<>();
        for (Map.Entry<String, String> entry : postParam.entrySet()) {
            keyValues.add(entry.getKey() + "=" + entry.getValue());
        }
        return CollectionUtils.join(keyValues, "&");
    }
}
