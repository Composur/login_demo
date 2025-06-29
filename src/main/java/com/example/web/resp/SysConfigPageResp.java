package com.example.web.resp;


import lombok.Data;

import java.util.Date;

@Data
public class SysConfigPageResp {
    private String id;
    private Integer type;
    private String code;
    private String value;
    private String memo;
    private String createdBy;
    private Date created;
    private String modifiedBy;
    private Date modified;
    private String typeName;
}
