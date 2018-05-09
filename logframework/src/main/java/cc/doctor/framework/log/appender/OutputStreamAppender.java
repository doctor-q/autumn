package cc.doctor.framework.log.appender;

import cc.doctor.framework.log.appender.encode.Encoder;
import com.alibaba.fastjson.annotation.JSONField;

public abstract class OutputStreamAppender implements Appender {
    private String name;
    @JSONField(deserialize = false)
    protected transient Encoder encoder;

    @Override
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Encoder getEncoder() {
        return encoder;
    }

    public void setEncoder(Encoder encoder) {
        this.encoder = encoder;
    }
}
