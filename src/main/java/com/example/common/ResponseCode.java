package com.example.common;

public final class ResponseCode {
    // 成功状态码
    public static final int SUCCESS = 1000;

    // 客户端错误状态码
    public static final int BAD_REQUEST = 40000;
    public static final int UNAUTHORIZED = 40100;
    public static final int FORBIDDEN = 40300;

    // 服务器错误状态码
    public static final int INTERNAL_SERVER_ERROR = 50000;

    // 私有构造函数，防止实例化
    private ResponseCode() {
        throw new UnsupportedOperationException("This class is a utility class and should not be instantiated.");
    }
}
