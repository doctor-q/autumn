package cc.doctor.framework.web.handler.resolver.json;

import cc.doctor.framework.utils.SerializeUtils;
import cc.doctor.framework.web.handler.resolver.Resolver;

/**
 * Created by doctor on 2017/7/21.
 */
public class JsonResolver implements Resolver {
    @Override
    public String resolve(Object data) {
        return SerializeUtils.objectToJson(data);
    }
}
