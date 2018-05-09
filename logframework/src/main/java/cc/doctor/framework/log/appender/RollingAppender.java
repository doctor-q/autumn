package cc.doctor.framework.log.appender;

import cc.doctor.framework.log.event.Event;
import cc.doctor.framework.log.rolling.RollingPolicy;
import com.alibaba.fastjson.annotation.JSONField;

public class RollingAppender extends OutputStreamAppender {
    @JSONField(deserialize = false)
    private transient RollingPolicy rollingPolicy;

    @Override
    public void append(Event event) {
        rollingPolicy.rollover();
    }

    public void setRollingPolicy(RollingPolicy rollingPolicy) {
        this.rollingPolicy = rollingPolicy;
    }
}
