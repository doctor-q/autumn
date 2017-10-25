package cc.doctor.framework.thrift;

/**
 * Created by doctor on 2017/7/15.
 */
public class ResponseUtils {
    public static Response successResponse() {
        Response response = new Response();
        response.setSuccess(true);
        return response;
    }

    public static Response successResponse(String data) {
        Response response = new Response();
        response.setSuccess(true);
        response.setData(data);
        return response;
    }

    public static Response errorResponse(String errorMsg) {
        Response response = new Response();
        response.setSuccess(false);
        response.setErrorMsg(errorMsg);
        return response;
    }

    public static Response errorResponse(Object errorMsg) {
        Response response = new Response();
        response.setSuccess(false);
        response.setErrorMsg(errorMsg.toString());
        return response;
    }

    public static Response errorResponse(int error, String errorMsg) {
        Response response = new Response();
        response.setSuccess(false);
        response.setError(error);
        response.setErrorMsg(errorMsg);
        return response;
    }
}
