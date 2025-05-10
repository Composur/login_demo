package com.example.config; // 您可以放在合适的包下，例如 config

import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler;
import com.example.security.utils.SecurityUtil; // 假设您的 SecurityUtil 在这里
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.reflection.MetaObject;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Slf4j
@Component // 将其注册为 Spring Bean
public class MyMetaObjectHandler implements MetaObjectHandler {

    @Override
    public void insertFill(MetaObject metaObject) {
        log.info("start insert fill ....");
        // 插入时填充 createdBy, created, modifiedBy, modified
        // 注意：这里的字段名 "createdBy", "created", "modifiedBy", "modified" 必须与实体类中的字段名一致
        // 如果实体类中没有这些字段，或者字段类型不匹配，这里 setFieldVal 会失败或报错

        // 检查字段是否存在，避免实体类没有该字段时出错
        if (metaObject.hasSetter("createdBy")) {
            this.strictInsertFill(metaObject, "createdBy", String.class, SecurityUtil.getCurrentUsername());
        }
        if (metaObject.hasSetter("created")) {
            this.strictInsertFill(metaObject, "created", LocalDateTime.class, LocalDateTime.now());
        }
        if (metaObject.hasSetter("modifiedBy")) {
            this.strictInsertFill(metaObject, "modifiedBy", String.class, SecurityUtil.getCurrentUsername());
        }
        if (metaObject.hasSetter("modified")) {
            this.strictInsertFill(metaObject, "modified", LocalDateTime.class, LocalDateTime.now());
        }
    }

    @Override
    public void updateFill(MetaObject metaObject) {
        log.info("start update fill ....");
        // 更新时填充 modifiedBy, modified
        if (metaObject.hasSetter("modifiedBy")) {
            this.strictUpdateFill(metaObject, "modifiedBy", String.class, SecurityUtil.getCurrentUsername());
        }
        if (metaObject.hasSetter("modified")) {
            this.strictUpdateFill(metaObject, "modified", LocalDateTime.class, LocalDateTime.now());
        }
    }
}