package cc.doctor.framework.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.UnsupportedEncodingException;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.UUID;

/**
 * Created by doctor on 2017/3/19.
 */
public class URIUtils {
    private static final Logger log = LoggerFactory.getLogger(URIUtils.class);

    public static String base64UUid() {
        UUID uuid = UUID.randomUUID();
        return uuid.toString();
    }

    public static String toNameValuePairString(Object obj) {
        StringBuilder paramStringBuilder = new StringBuilder();
        Field[] declaredFields = obj.getClass().getDeclaredFields();
        for (Field declaredField : declaredFields) {
            if (!Modifier.isStatic(declaredField.getModifiers())) {
                String fieldName = declaredField.getName();
                String getMethod = "get" + fieldName.substring(0, 1).toUpperCase() + fieldName.substring(1);
                try {
                    Method method = obj.getClass().getMethod(getMethod);
                    Object ret = method.invoke(obj);
                    if (ret != null) {
                        paramStringBuilder.append(fieldName).append("=").append(ret.toString()).append("&");
                    }
                } catch (Exception e) {
                    log.error("", e);
                }
            }
        }
        if (paramStringBuilder.length() > 0) {
            try {
                return URLEncoder.encode(paramStringBuilder.toString().substring(0, paramStringBuilder.length() - 1), "UTF-8");
            } catch (UnsupportedEncodingException e) {
                log.error("", e);
            }
        }
        return "";
    }

    public static Map<String, String> toNameValuePair(String kvString) {
        try {
            kvString = URLDecoder.decode(kvString, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            log.error("", e);
        }
        Map<String, String> kvMap = new LinkedHashMap<>();
        String[] split = kvString.split("&");
        for (String kv : split) {
            String[] keyValue = kv.split("=");
            if (keyValue.length == 2) {
                kvMap.put(keyValue[0], keyValue[1]);
            }
        }
        return kvMap;
    }
}
