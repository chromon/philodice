package com.philodice.common.response;

/**
 * 通用的响应消息格式
 * @param <T> 成功时返回的响应数据
 */
public class ResponseMessage<T> {

    /**
     * 响应码，200 表示成功，其他值表示错误
     */
    private int code;

    /**
     * 响应消息
     */
    private String message;

    /**
     * 响应数据
     */
    private T data;

    public ResponseMessage() {}

    public ResponseMessage(int code, String message, T data) {
        this.code = code;
        this.message = message;
        this.data = data;
    }

    /**
     * 成功时返回的响应消息
     * @param data 响应数据
     * @return ResponseMessage
     * @param <T> 响应数据类型
     */
    public static <T> ResponseMessage<T> success(T data) {
        return new ResponseMessage<>(200, "Success", data);
    }

    /**
     * 出错时返回的响应消息
     * @param code 响应码
     * @param message 错误消息
     * @return ResponseMessage
     * @param <T> 响应数据类型，当前为空
     */
    public static <T> ResponseMessage<T> error(int code, String message) {
        return new ResponseMessage<>(code, message, null);
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }
}
