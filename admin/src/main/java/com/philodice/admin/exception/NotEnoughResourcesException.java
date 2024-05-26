package com.philodice.admin.exception;

import com.philodice.common.exception.BaseException;

/**
 * 自定义异常类：服务器资源不足异常，如没有足够的序列号等
 */
public class NotEnoughResourcesException extends BaseException {
    public NotEnoughResourcesException(String message) {
        super(500, message);
    }
}
