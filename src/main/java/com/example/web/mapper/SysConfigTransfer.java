package com.example.web.mapper;

import com.example.dal.entity.SysConfigEntity;
import com.example.service.dto.SysConfigDTO;
import com.example.web.resp.PageResult;
import com.example.web.resp.SysConfigPageResp;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper
public interface SysConfigTransfer {
    SysConfigTransfer INSTANCE = Mappers.getMapper(SysConfigTransfer.class);

    SysConfigDTO toSysConfigDTO(SysConfigEntity sysConfigEntity);

    // 单个对象转换
    SysConfigPageResp toSysConfigPageResp(SysConfigDTO sysConfigDTO);

    // 列表转换
    List<SysConfigPageResp> toRespList(List<SysConfigDTO> dtoList);

    // 分页转换（推荐）
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
}
