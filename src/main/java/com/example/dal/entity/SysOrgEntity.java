package com.example.dal.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;
// import java.time.LocalDateTime; // LocalDateTime 已由 BaseEntity 引入

/**
 * 系统机构实体类
 */
@Data
// callSuper = true 表示 equals 和 hashCode 方法会包含父类的字段
@EqualsAndHashCode(callSuper = true)
@TableName("sys_org")
// 继承 BaseEntity
public class SysOrgEntity extends BaseEntity {

    // private static final long serialVersionUID = 1L; // 可以移除，使用父类的

    /**
     * 主键ID (覆盖父类的定义以匹配表结构)
     * 注意：父类 BaseEntity 的 id 是 String 类型，这里覆盖为 Long 类型以匹配 sys_org 表
     * MyBatis-Plus 会优先使用子类的 @TableId 注解
     */
    //@TableId(value = "id", type = IdType.AUTO)
    //private Long id; // 使用 Long 类型并指定自增

    // --- 以下字段已从 BaseEntity 继承，在此处移除 ---
    // private Long createdBy;
    // private LocalDateTime createdTime; // 使用父类的 created 字段
    // private Long modifiedBy;
    // private LocalDateTime modifiedTime; // 使用父类的 modified 字段
    // --- 移除结束 ---

    /**
     * 父机构ID
     */
    @TableField("parent_id")
    private Long parentId;

    /**
     * 机构路径 (例如 /1/3/5/)
     */
    @TableField("path")
    private String path;

    /**
     * 机构编码
     */
    @TableField("code")
    private String code;

    /**
     * 机构名称
     */
    @TableField("name")
    private String name;

    /**
     * 联系人
     */
    @TableField("link_man")
    private String linkMan;

    /**
     * 联系电话
     */
    @TableField("link_tel")
    private String linkTel;

    /**
     * 省份ID
     */
    @TableField("province_id")
    private Long provinceId;

    /**
     * 省份名称
     */
    @TableField("province")
    private String province;

    /**
     * 城市ID
     */
    @TableField("city_id")
    private Long cityId;

    /**
     * 城市名称
     */
    @TableField("city")
    private String city;

    /**
     * 区县ID
     */
    @TableField("county_id")
    private Long countyId;

    /**
     * 区县名称
     */
    @TableField("county")
    private String county;

    /**
     * 详细地址
     */
    @TableField("address")
    private String address;

    /**
     * 经度
     */
    @TableField("longitude")
    private BigDecimal longitude;

    // 注意：通常经度 (longitude) 会和纬度 (latitude) 一起出现，请确认表中是否有 latitude 字段

    /**
     * 备注
     */
    @TableField("memo")
    private String memo;

    /**
     * 是否系统内置 (1:是, 0:否)
     */
    @TableField("is_system")
    private Boolean isSystem;

    /**
     * 是否启用 (1:是, 0:否)
     */
    @TableField("is_enable")
    private Boolean isEnable;

    // --- 需要确保 BaseEntity 中的字段与数据库列名正确映射 ---
    // BaseEntity 中的 createdBy 对应数据库列 created_by
    // BaseEntity 中的 created 对应数据库列 created
    // BaseEntity 中的 modifiedBy 对应数据库列 modified_by
    // BaseEntity 中的 modified 对应数据库列 modified
    // 如果 BaseEntity 中没有使用 @TableField 指定列名，需要确保 MyBatis-Plus 的全局配置
    // (如 mapUnderscoreToCamelCase=true) 或在 BaseEntity 中添加 @TableField 注解。
    // 假设 BaseEntity 已正确处理或配置了映射关系。
}
