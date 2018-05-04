package cc.doctor.framework.log.appender.encode;

import cc.doctor.framework.log.event.Event;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectOutput;
import java.io.ObjectOutputStream;

public class ObjectEncoder implements Encoder {
    @Override
    public byte[] encode(Event event) {
        try {
            return serialize(event);
        } catch (IOException e) {
            return null;
        }
    }

    //序列化
    public static byte[] serialize(Object object) throws IOException {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        ObjectOutput objectOutput = new ObjectOutputStream(outputStream);
        objectOutput.writeObject(object);
        return outputStream.toByteArray();
    }

}
