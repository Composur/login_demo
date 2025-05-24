package com.example.common.enums;

public enum MenuType {
    DIRECTORY(0, "目录"),
    MENU(1, "菜单"),
    BUTTON(2, "按钮权限");

    private final int code;
    private final String name;

    MenuType(int code, String name) {
        this.code = code;
        this.name = name;
    }

    public int getCode() {
        return code;
    }

    public String getName() {
        return name;
    }

    public static String getNameByCode(Integer code) {
        if (code == null) {
            return "";
        }
        for (MenuType type : MenuType.values()) {
            if (type.getCode() == code) {
                return type.getName();
            }
        }
        return "";
    }
}