package com.example.common;

import org.springframework.dao.DuplicateKeyException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.validation.ConstraintViolationException;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    @ResponseBody
    public Response<?> handleMethodNotSupported(HttpRequestMethodNotSupportedException ex) {
        return Response.error(405, "不支持的请求方法，请使用正确的请求方式");
    }

    @ExceptionHandler(ConstraintViolationException.class)
    public Response handleConstraintViolationException(ConstraintViolationException ex) {
        // 只取第一个错误信息
        String errorMsg = ex.getConstraintViolations().iterator().next().getMessage();
        return Response.error(ErrorCode.PARAM_ERROR.getCode(), errorMsg);
    }

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
