package cc.doctor.framework.web.handler.parser;

import cc.doctor.framework.web.servlet.meta.HttpMetadata;

/**
 * Created by doctor on 2017/5/21.
 * unpack参数，可以调用封装前后的钩子
 */
public abstract class Unpack {
    /**
     * before hook
     * @param httpMetadata http元数据
     */
    public void beforeUnpack(HttpMetadata httpMetadata) {}

    /**
     * after hook
     * @param httpMetadata http 元数据
     */
    public void afterUnpack(HttpMetadata httpMetadata) {}
}
