package com.example.common;

import org.springframework.dao.DuplicateKeyException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(IllegalArgumentException.class)
    public Response<?> handleIllegalArgument(IllegalArgumentException ex) {
        return Response.error(ErrorCode.PARAM_ERROR.getCode(), ex.getMessage());
    }

    @ExceptionHandler(BusinessException.class)
    public Response<?> handleBusinessException(BusinessException ex) {
        return Response.error(ex.getCode(), ex.getMessage());
    }

    @ExceptionHandler(DuplicateKeyException.class)
    public Response<?> handleDuplicateKey(DuplicateKeyException ex) {
        return Response.error("该数据已存在，不能重复提交");
    }

    @ExceptionHandler(Exception.class)
    public Response<?> handleAll(Exception ex) {
        ex.printStackTrace(); // 或用 logger
        return Response.error(ErrorCode.SYSTEM_ERROR.getCode(), "服务器发生错误：" + ex.getMessage());
    }
}
