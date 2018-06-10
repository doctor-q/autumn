package cc.doctor.framework.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;

/**
 * Created by doctor on 2017/3/8.
 */
public class SerializeUtils {
    private static final Logger log = LoggerFactory.getLogger(SerializeUtils.class);

    //序列化
    public static <T extends Serializable> byte[] serialize(T serializable) throws IOException {
        try {
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            ObjectOutput objectOutput = new ObjectOutputStream(outputStream);
            objectOutput.writeObject(serializable);
            return outputStream.toByteArray();
        } catch (IOException e) {
            log.error("", e);
            throw e;
        }
    }

    //反序列化
    public static <T extends Serializable> T deserialize(byte[] bytes) throws IOException, ClassNotFoundException {
        ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(bytes);
        try {
            ObjectInput objectInput = new ObjectInputStream(byteArrayInputStream);
            return (T) objectInput.readObject();
        } catch (Exception e) {
            log.error("", e);
            throw e;
        }
    }
}
