package com.example.dal.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import java.io.Serializable;
import java.time.LocalDateTime;

@Data
@EqualsAndHashCode
@ToString
public class BaseEntity implements Serializable {

    /**
     * ID
     */
    @TableId(type = IdType.ASSIGN_ID)
    //@Schema(description = "id")
    protected String id;

    /**
     * 创建人
     */
    //@Schema(description = "创建人")
    protected String createdBy;

    /**
     * 创建时间
     */
    //@Schema(description = "创建时间")
    protected LocalDateTime created;

    /**
     * 更新人
     */
    //@Schema(description = "更新人")
    protected String modifiedBy;

    /**
     * 更新时间
     */
    //@Schema(description = "更新时间")
    protected LocalDateTime modified;
}
