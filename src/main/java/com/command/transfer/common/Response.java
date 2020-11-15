package com.command.transfer.common;

import java.io.Serializable;

/**
 * Response
 *
 * @author liulei
 * @date 2020/11/6
 */
public class Response<T> implements Serializable {
    private static final long serialVersionUID = -5141211545674454438L;
    private int code;
    private boolean success;
    private String message;
    private T data;

    public Response() {
    }

    public static Response buildFailure(Integer errCode, String errMessage) {
        Response response = new Response();
        response.setSuccess(false);
        response.setCode(errCode);
        response.setMessage(errMessage);
        return response;
    }

    public static Response buildSuccess() {
        Response response = new Response();
        response.setSuccess(true);
        return response;
    }

    public static Response error(ErrorCode errorCode, Object detail) {
        Response res = new Response();
        res.setCode(errorCode.getCode());
        res.setMessage(errorCode.getMessage());
        res.setData(detail);
        return res;
    }

    public static Response error(Integer errorCode, String errorMessage, Object detail) {
        Response res = new Response();
        res.setCode(errorCode);
        res.setMessage(errorMessage);
        res.setData(detail);
        return res;
    }

    public static Response result(Integer code, String message, Object detail) {
        Response res = new Response();
        res.setCode(code);
        res.setMessage(message);
        res.setData(detail);
        return res;
    }

    public static Response success(Object data) {
        Response res = new Response();
        res.setCode(ErrorCode.SUCCESS.getCode());
        res.setMessage(ErrorCode.SUCCESS.getMessage());
        res.setData(data);
        return res;
    }

    public int getCode() {
        return this.code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMessage() {
        return this.message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public T getData() {
        return this.data;
    }

    public void setData(T data) {
        this.data = data;
    }

    public boolean isSuccess() {
        return this.success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public String toString() {
        return "{code=" + this.code + ", message='" + this.message + '\'' + ", data=" + this.data + '}';
    }

}
