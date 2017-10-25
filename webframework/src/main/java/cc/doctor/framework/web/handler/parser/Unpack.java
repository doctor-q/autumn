package cc.doctor.framework.web.handler.parser;

import cc.doctor.framework.web.servlet.meta.HttpMetaData;
import cc.doctor.framework.web.servlet.meta.HttpMetaParameters;

import javax.servlet.http.HttpServletRequest;

/**
 * Created by doctor on 2017/5/21.
 */
public abstract class Unpack {
    public void beforeUnpack(HttpMetaData httpMetaData, HttpMetaParameters httpMetaParameters) {}
    public void afterUnpack(HttpMetaData httpMetaData, HttpMetaParameters httpMetaParameters) {}
}
