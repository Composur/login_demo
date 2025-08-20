package com.example.web.mapper;

import com.example.dal.entity.QuartzJobEntity;
import com.example.service.dto.QuartzJobDTO;
import com.example.web.resp.PageResult;
import com.example.web.resp.QuartzJobQueryPageResp;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper
public interface QuartzJobTransfer {
    QuartzJobTransfer INSTANCE = Mappers.getMapper(QuartzJobTransfer.class);

    QuartzJobEntity toQuartzJobEntity(QuartzJobDTO quartzJobDTO);

    QuartzJobDTO toQuartzJobDTO(QuartzJobEntity quartzJobEntity);

    // 单个对象转换
    QuartzJobQueryPageResp toQuartzJobQueryPageResp(QuartzJobDTO quartzJobDTO);

    // 列表转换
    List<QuartzJobQueryPageResp> toRespList(List<QuartzJobDTO> dtoList);

    // 分页转换
    default PageResult<QuartzJobQueryPageResp> toRespPage(PageResult<QuartzJobDTO> dtoPage) {
        if (dtoPage == null) {
            return null;
        }

        List<QuartzJobQueryPageResp> respList = toRespList(dtoPage.getRecords());

        PageResult<QuartzJobQueryPageResp> respPage = new PageResult<>();
        respPage.setRecords(respList);
        respPage.setTotal(dtoPage.getTotal());
        respPage.setCurrent(dtoPage.getCurrent());
        respPage.setSize(dtoPage.getSize());

        return respPage;
    }
} 