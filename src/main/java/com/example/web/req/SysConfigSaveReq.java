package com.example.web.req;

import lombok.Data;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
public class SysConfigSaveReq {

    @NotNull(message = "类型不能为空")
    @Min(value = 1, message = "类型错误")
    @Max(value = 2, message = "类型错误")
    private Integer type; // 只能是1或2

    @NotBlank(message = "配置编码不能为空")
    private String code;

    @NotBlank(message = "配置值不能为空")
    private String value;

    private String memo;
}
