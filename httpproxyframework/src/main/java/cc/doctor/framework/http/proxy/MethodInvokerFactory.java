package cc.doctor.framework.http.proxy;

import cc.doctor.framework.http.proxy.annotation.*;
import org.apache.http.entity.ContentType;

import java.lang.reflect.Method;

public class MethodInvokerFactory {
    private static MethodInvokerFactory instance;

    public static MethodInvokerFactory getInstance() {
        if (instance == null) {
            instance = new MethodInvokerFactory();
        }
        return instance;
    }

    private MethodInvokerFactory() {
    }

    public MethodInvoker methodInvoker(Method method) {
        MethodInvoker methodInvoker;
        if (method.isAnnotationPresent(Get.class)) {
            Get get = method.getAnnotation(Get.class);
            methodInvoker = getMethodInvoker(get);
        } else if (method.isAnnotationPresent(Post.class)) {
            Post post = method.getAnnotation(Post.class);
            methodInvoker = postMethodInvoker(post);
        } else if (method.isAnnotationPresent(PostJson.class)) {
            PostJson postJson = method.getAnnotation(PostJson.class);
            methodInvoker = postMethodInvoker(postJson);
        } else if (method.isAnnotationPresent(FormData.class)) {
            FormData formData = method.getAnnotation(FormData.class);
            methodInvoker = postMethodInvoker(formData);
        } else {
            throw new MethodNotSupportException();
        }
        return methodInvoker;
    }

    private PostMethodInvoker postMethodInvoker(PostJson postJson) {
        PostMethodInvoker postMethodInvoker = new PostMethodInvoker();
        postMethodInvoker.setContentType(ContentType.APPLICATION_JSON);
        postMethodInvoker.setSubUrl(postJson.value());
        return postMethodInvoker;
    }

    private PostMethodInvoker postMethodInvoker(Post post) {
        PostMethodInvoker postMethodInvoker = new PostMethodInvoker();
        postMethodInvoker.setContentType(ContentType.APPLICATION_FORM_URLENCODED);
        postMethodInvoker.setSubUrl(post.value());
        return postMethodInvoker;
    }

    private PostMethodInvoker postMethodInvoker(FormData formData) {
        PostMethodInvoker postMethodInvoker = new PostMethodInvoker();
        postMethodInvoker.setContentType(ContentType.MULTIPART_FORM_DATA);
        postMethodInvoker.setSubUrl(formData.value());
        postMethodInvoker.setMultipart(true);
        return postMethodInvoker;
    }

    private GetMethodInvoker getMethodInvoker(Get get) {
        GetMethodInvoker getMethodInvoker = new GetMethodInvoker();
        getMethodInvoker.setSubUrl(get.value());
        return getMethodInvoker;
    }
}
