package com.philodice.common.exception;

/**
 * 自定义异常基类
 */
public class BaseException extends RuntimeException {

    /**
     * 异常状态码
     */
    private final int code;

    public BaseException(int code, String message) {
        super(message);
        this.code = code;
    }

    /**
     * 获取异常状态码
     * @return 状态码
     */
    public int getCode() {
        return code;
    }
}
