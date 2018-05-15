package cc.doctor.framework.log.appender;

import cc.doctor.framework.log.event.Event;
import cc.doctor.framework.log.rolling.RollingPolicy;
import com.alibaba.fastjson.annotation.JSONField;

import java.io.FileOutputStream;
import java.io.IOException;

public class RollingAppender extends OutputStreamAppender {
    @JSONField(deserialize = false)
    private transient RollingPolicy rollingPolicy;

    @Override
    public void append(Event event) {
        rollingPolicy.rollover();
        FileOutputStream current = rollingPolicy.getCurrent();
        try {
            current.write(encoder.encode(event));
            current.write(System.lineSeparator().getBytes());
        } catch (IOException e) {

        }
    }

    public void setRollingPolicy(RollingPolicy rollingPolicy) {
        this.rollingPolicy = rollingPolicy;
    }
}
