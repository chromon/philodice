package com.philodice.admin.exception;

import com.philodice.common.exception.BaseException;

/**
 * 自定义异常类：Redis 序列号连接池初始化异常
 */
public class SequencePoolInitializeException extends BaseException {
    public SequencePoolInitializeException(String message) {
        super(500, message);
    }
}
