package com.philodice.common.exception;

/**
 * 错误响应实体类
 */
public class ErrorResponse {

    /**
     * 状态码
     */
    private int code;

    /**
     * 错误信息
     */
    private String message;

    public ErrorResponse(int code, String message) {
        this.code = code;
        this.message = message;
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
}
