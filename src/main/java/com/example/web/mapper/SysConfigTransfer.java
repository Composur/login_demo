package com.example.web.mapper;

import com.example.common.enums.ConfigTypeEnum;
import com.example.dal.entity.SysConfigEntity;
import com.example.service.dto.SysConfigDTO;
import com.example.web.req.SysConfigSaveReq;
import com.example.web.resp.PageResult;
import com.example.web.resp.SysConfigPageResp;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper
public interface SysConfigTransfer {
    SysConfigTransfer INSTANCE = Mappers.getMapper(SysConfigTransfer.class);

    SysConfigEntity toSysConfigEntity(SysConfigDTO sysConfigDTO);

    SysConfigDTO toSysConfigDTO(SysConfigEntity sysConfigEntity);

    // req 转 DTO
    SysConfigDTO toSysConfigDTO(SysConfigSaveReq req);

    // 单个对象转换，typeName 由 type 决定
    @Mappings({
            @Mapping(target = "typeName", expression = "java(com.example.web.mapper.SysConfigTransfer.typeToName(sysConfigDTO.getType()))")
    })
    SysConfigPageResp toSysConfigPageResp(SysConfigDTO sysConfigDTO);

    // 列表转换
    List<SysConfigPageResp> toRespList(List<SysConfigDTO> dtoList);

    // 分页转换
    default PageResult<SysConfigPageResp> toRespPage(PageResult<SysConfigDTO> dtoPage) {
        if (dtoPage == null) {
            return null;
        }

        List<SysConfigPageResp> respList = toRespList(dtoPage.getRecords());

        PageResult<SysConfigPageResp> respPage = new PageResult<>();
        respPage.setRecords(respList);
        respPage.setTotal(dtoPage.getTotal());
        respPage.setCurrent(dtoPage.getCurrent());
        respPage.setSize(dtoPage.getSize());

        return respPage;
    }

    // type转typeName的静态方法
    static String typeToName(Integer type) {
        if (type == null) return null;
        return ConfigTypeEnum.getNameByCode(type);
    }
}