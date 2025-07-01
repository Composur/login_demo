package com.example.common.enums;

public enum ConfigTypeEnum {
    SYSTEM(1, "系统配置"),
    BUSINESS(2, "业务配置");

    private final int code;
    private final String name;

    ConfigTypeEnum(int code, String name) {
        this.code = code;
        this.name = name;
    }

    public static String getNameByCode(Integer code) {
        if (code == null) return null;
        for (ConfigTypeEnum e : values()) {
            if (e.code == code) return e.name;
        }
        return "未知";
    }
}
