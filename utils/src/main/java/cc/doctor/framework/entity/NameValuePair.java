package cc.doctor.framework.entity;

import cc.doctor.framework.utils.CollectionUtils;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

public class NameValuePair {
    private String name;
    private Object value;

    public NameValuePair() {
    }

    public NameValuePair(String name, Object value) {
        this.name = name;
        this.value = value;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Object getValue() {
        return value;
    }

    public void setValue(Object value) {
        this.value = value;
    }

    @Override
    public String toString() {
        if (name == null) {
            return "";
        }
        try {
            return name + "=" + URLEncoder.encode(value == null ? "" : value.toString(), "UTF-8");
        } catch (UnsupportedEncodingException e) {
            return "";
        }
    }

    public static String toNameValuesString(Iterable<NameValuePair> nameValuePairs) {
        Iterable<String> transform = CollectionUtils.transform(nameValuePairs, new Function<NameValuePair, String>() {
            @Override
            public String transform(NameValuePair from) {
                return from.toString();
            }
        });
        return CollectionUtils.join(transform, "&");
    }
}
