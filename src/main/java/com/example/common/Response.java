package com.example.common;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

import java.util.List;

@Data // Lombok 注解，生成 getter/setter
@JsonInclude(JsonInclude.Include.NON_NULL) // 不返回 null 字段
public class Response<T> {
    private Boolean success;

    // 状态码（20000 表示成功，其他为错误码）
    private int code;

    // 状态描述
    private String message;

    // 业务数据（泛型）
    private T result;

    // 错误详情（失败时填充）
    private List<Error> errors;

    // 成功时的构造方法
    public static <T> Response<T> success(T result) {
        return new Response<>(true, ResponseCode.SUCCESS, "成功", result, null);
    }

    // 无数据的成功（如删除操作）
    public static Response<?> success() {
        return new Response<>(true, ResponseCode.SUCCESS, "成功", null, null);
    }

    // 失败时的构造方法（带错误信息）
    public static Response<?> error(Integer code, String message, List<Error> errors) {
        return new Response<>(false, code, message, null, errors);
    }

    // 私有构造方法，强制使用静态工厂方法
    private Response(Boolean success, int code, String message, T result, List<Error> errors) {
        this.code = code;
        this.message = message;
        this.result = result;
        this.errors = errors;
        this.success = success;
    }
}