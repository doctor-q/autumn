package cc.doctor.framework.web.handler.resolver.json;

import cc.doctor.framework.web.handler.resolver.Resolver;
import com.alibaba.fastjson.JSONObject;

/**
 * Created by doctor on 2017/7/21.
 */
public class JsonResolver implements Resolver {

    public String resolve(Object data) {
        return JSONObject.toJSONString(data);
    }

    @Override
    public String getName() {
        return "json";
    }
}
