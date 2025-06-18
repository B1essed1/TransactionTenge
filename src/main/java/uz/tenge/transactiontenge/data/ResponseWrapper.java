package uz.tenge.transactiontenge.data;

import lombok.Data;

@Data
public class ResponseWrapper<T> {
    private T data;
    private String message;
    private Integer code;

    public static ResponseWrapper internalServer(String ex) {
        ResponseWrapper<?> responseWrapper = new ResponseWrapper();
        responseWrapper.setCode(500);
        responseWrapper.setMessage(ex);
        return responseWrapper;
    }

    public static ResponseWrapper badRequest(String ex) {
        ResponseWrapper<?> responseWrapper = new ResponseWrapper();
        responseWrapper.setCode(400);
        responseWrapper.setMessage(ex);
        return responseWrapper;
    }


    public static ResponseWrapper notFound(String ex) {
        ResponseWrapper<?> responseWrapper = new ResponseWrapper();
        responseWrapper.setCode(404);
        responseWrapper.setMessage(ex);
        return responseWrapper;
    }

    public static ResponseWrapper allTypeException(String ex, Integer code) {
        ResponseWrapper<?> responseWrapper = new ResponseWrapper();
        responseWrapper.setCode(code);
        responseWrapper.setMessage(ex);
        return responseWrapper;
    }
}
