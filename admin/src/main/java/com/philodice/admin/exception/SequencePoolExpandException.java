package com.philodice.admin.exception;

import com.philodice.common.exception.BaseException;

/**
 * 自定义异常类：Redis 序列号连接池扩容异常
 */
public class SequencePoolExpandException extends BaseException {
    public SequencePoolExpandException(String message) {
        super(500, message);
    }
}
