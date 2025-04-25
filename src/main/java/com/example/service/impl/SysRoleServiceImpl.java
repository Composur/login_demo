package com.example.service.impl;

import com.example.service.SysRoleService;
import com.example.service.dto.SysRoleDTO;
import com.example.web.mapper.SysRoleTransfer;
import com.example.web.resp.SysRoleResp;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class SysRoleServiceImpl implements SysRoleService {

    // 注入 Mapper (或者使用构造函数注入)
    @Autowired
    //private SysRoleMapper sysRoleMapper;

    // 注入 DTO 和 Entity 之间的转换器 (假设你也有一个)
    // @Autowired
    // private YourEntityDtoMapper entityDtoMapper;

    @Override
    public String save() { // 调整了 save 方法签名以匹配接口
        // 实现保存逻辑...
        return "1"; // 示例返回
    }

    @Override // 确保实现了接口方法
    public List<SysRoleResp> queryList() {
        // 1. 从数据库查询 Entity 列表 (需要先创建 SysRoleMapper)
        // List<SysRoleEntity> entityList = sysRoleMapper.selectAll(); // 假设 Mapper 有 selectAll 方法

        // 2. 将 Entity 列表转换为 DTO 列表 (需要转换逻辑, 可能用 MapStruct)
        // List<SysRoleDTO> sysRoleDTOList = entityList.stream()
        //        .map(entity -> entityDtoMapper.toDto(entity)) // 假设有 entity 到 dto 的转换
        //        .collect(Collectors.toList());

        // --- 临时的示例数据，你需要替换上面两步 ---
        List<SysRoleDTO> sysRoleDTOList = Collections.emptyList();
        // --- 临时的示例数据结束 ---


        // 3. 将 DTO 列表转换为 Resp 列表 (修正错误)
        List<SysRoleResp> sysRoleRespList = sysRoleDTOList.stream()
                .map(sysRoleDTO -> SysRoleTransfer.INSTANCE.toSysRoleResp(sysRoleDTO))
                .collect(Collectors.toList()); // 添加 .collect(Collectors.toList())

        return sysRoleRespList;
    }
}
