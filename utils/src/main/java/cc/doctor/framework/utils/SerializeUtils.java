package cc.doctor.framework.utils;

import com.google.gson.Gson;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.List;

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

    public static <T> T jsonToObject(String json, Class<T> clazz) {
        Gson gson = new Gson();
        return gson.fromJson(json, clazz);
    }

    public static <T> List<T> jsonToList(String json, final Class<T> clazz) {
        Gson gson = new Gson();
        // 生成List<T> 中的 List<T>
        return gson.fromJson(json, new ListOfT<>(clazz));
    }

    public static <T> String objectToJson(T t) {
        Gson gson = new Gson();
        return gson.toJson(t);
    }

    static class ListOfT<T> implements ParameterizedType {

        private Class<?> wrapped;

        public ListOfT(Class<T> wrapped) {
            this.wrapped = wrapped;
        }

        public Type[] getActualTypeArguments() {
            return new Type[] { wrapped };
        }

        public Type getRawType() {
            return List.class;
        }

        public Type getOwnerType() {
            return null;
        }
    }
}
