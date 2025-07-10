package com.example.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.config.cache.ConfigCacheProvider;
import com.example.dal.entity.SysConfigEntity;
import com.example.dal.mapper.SysConfigMapper;
import com.example.service.SysConfigService;
import com.example.service.dto.SysConfigDTO;
import com.example.web.controller.SysConfigPageReq;
import com.example.web.mapper.SysConfigTransfer;
import com.example.web.resp.PageResult;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class SysConfigServiceImpl implements SysConfigService {

    private final SysConfigMapper sysConfigMapper;

    private final ConfigCacheProvider configCacheProvider;

    @Override
    public PageResult<SysConfigDTO> queryConfigByPage(SysConfigPageReq req) {
        Page<SysConfigEntity> page = new Page<>(req.getCurrent(), req.getSize());
        IPage<SysConfigEntity> sysConfigEntities = sysConfigMapper.selectConfigPage(page, req);
        List<SysConfigDTO> sysConfigDTOS = sysConfigEntities.getRecords().stream().map(SysConfigTransfer.INSTANCE::toSysConfigDTO).collect(Collectors.toList());
        PageResult<SysConfigDTO> respPage = new PageResult<>(sysConfigDTOS, sysConfigEntities.getTotal(), sysConfigEntities.getCurrent(), sysConfigEntities.getSize());
        return respPage;
    }

    /**
     * @param sysConfigDTO
     * @return
     */
    @Override
    public SysConfigDTO save(SysConfigDTO sysConfigDTO) {
        SysConfigEntity sysConfigEntity = SysConfigTransfer.INSTANCE.toSysConfigEntity(sysConfigDTO);
        int insert = sysConfigMapper.insert(sysConfigEntity);
        configCacheProvider.cacheConfig(sysConfigEntity.getCode(), sysConfigEntity.getValue());
        return insert > 0 ? SysConfigTransfer.INSTANCE.toSysConfigDTO(sysConfigEntity) : null;
    }

    /**
     * @param id
     * @return
     */
    @Override
    public SysConfigDTO update(String id, SysConfigDTO sysConfigDTO) {
        SysConfigEntity sysConfigEntity = sysConfigMapper.selectById(id);
        if (sysConfigEntity == null) {
            throw new RuntimeException("未找到对应的配置，id=" + id);
        }
        SysConfigEntity updateEntity = SysConfigTransfer.INSTANCE.toSysConfigEntity(sysConfigDTO);
        updateEntity.setId(id);
        int i = sysConfigMapper.updateById(updateEntity);
        // 更新缓存
        configCacheProvider.cacheConfig(updateEntity.getCode(), updateEntity.getValue());
        return i > 0 ? SysConfigTransfer.INSTANCE.toSysConfigDTO(updateEntity) : null;
    }

    /**
     * @param ids
     */
    @Override
    public void deleteByIds(Set<String> ids) {
        List<SysConfigEntity> sysConfigEntities = sysConfigMapper.selectBatchIds(ids);

        sysConfigMapper.deleteBatchIds(ids);
        
        // 删除缓存
        for (SysConfigEntity sysConfigEntity : sysConfigEntities) {
            configCacheProvider.clearConfig(sysConfigEntity.getCode());
        }
    }

    /**
     * 刷新缓存
     *
     * @param id
     */
    @Override
    public void refreshCache(String id) {
        SysConfigEntity sysConfigEntity = sysConfigMapper.selectById(id);
        if (sysConfigEntity == null) {
            throw new RuntimeException("未找到对应的配置，id=" + id);
        }
        configCacheProvider.cacheConfig(sysConfigEntity.getCode(), sysConfigEntity.getValue());
    }

    /**
     * 从数据库重新加载所有配置并更新缓存
     */
    @Override
    public void refreshAllCache() {
        List<SysConfigEntity> sysConfigEntities = sysConfigMapper.selectList(null);
        for (SysConfigEntity sysConfigEntity : sysConfigEntities) {
            configCacheProvider.cacheConfig(sysConfigEntity.getCode(), sysConfigEntity.getValue());
        }
    }
}
