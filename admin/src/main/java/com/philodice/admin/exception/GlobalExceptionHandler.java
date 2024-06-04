package com.philodice.admin.exception;

import com.philodice.common.exception.ErrorResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

/**
 * 全局异常处理器，用于捕获并处理各种异常
 * 根据不同的异常类型返回不同的响应内容和 HTTP 状态码。
 * ControllerAdvice 标识这是一个全局异常处理器
 */
@ControllerAdvice
public class GlobalExceptionHandler {

    private final Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler .class);

    /**
     * 处理 NotEnoughResourcesException 异常
     */
    @ExceptionHandler(NotEnoughResourcesException.class)
    public ResponseEntity<ErrorResponse> handleResourceNotFound(NotEnoughResourcesException e) {
        ErrorResponse errorResponse = new ErrorResponse(e.getCode(), e.getMessage());
        return new ResponseEntity<>(errorResponse, HttpStatus.NOT_FOUND);
    }

    /**
     * 处理 SeqPoolInitializeException 异常
     */
    @ExceptionHandler(SeqPoolInitializeException.class)
    public ResponseEntity<ErrorResponse> handleSequencePoolInitialize(SeqPoolInitializeException e) {
        ErrorResponse errorResponse = new ErrorResponse(e.getCode(), e.getMessage());
        return new ResponseEntity<>(errorResponse, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    /**
     * 处理 SeqPoolExpandException 异常
     */
    @ExceptionHandler(SeqPoolExpandException.class)
    public ResponseEntity<ErrorResponse> handleSequencePoolExpand(SeqPoolExpandException e) {
        ErrorResponse errorResponse = new ErrorResponse(e.getCode(), e.getMessage());
        return new ResponseEntity<>(errorResponse, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    /**
     * 处理其他未知异常
     * @param e 异常
     * @return 响应实体
     */
    public ResponseEntity<ErrorResponse> handleException(Exception e) {

        // 记录异常日志
        logger.error("Uncaught exception: ", e);

        ErrorResponse errorResponse = new ErrorResponse(500, "An unexpected error occurred: " + e);
        return new ResponseEntity<>(errorResponse, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
