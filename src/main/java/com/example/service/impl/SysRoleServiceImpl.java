package com.example.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.dal.entity.SysRoleEntity;
import com.example.dal.mapper.SysRoleMapper;
import com.example.security.utils.SecurityUtil;
import com.example.service.SysRoleService;
import com.example.service.dto.RoleQueryDTO;
import com.example.service.dto.SysRoleDTO;
import com.example.service.dto.UserDTO;
import com.example.web.mapper.SysRoleTransfer;
import com.example.web.resp.PageResult;
import com.example.web.resp.SysRoleResp;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
@Slf4j
public class SysRoleServiceImpl implements SysRoleService {

    private final SysRoleMapper sysRoleMapper;

    @Override
    public String save() {
        // 实现保存逻辑...
        return "1"; // 示例返回
    }

    @Override
    public List<SysRoleResp> queryList(RoleQueryDTO queryDTO) {
        UserDTO currentUser = SecurityUtil.getCurrentUser();

        // 如果用户不存在，返回空列表
        if (currentUser == null) {
            log.warn("无法获取当前用户信息，返回空角色列表");
            return Collections.emptyList();
        }

        // 根据用户类型决定查询方式
        if (currentUser.isManger()) {
            queryDTO.setOnlyOwnRoles(false);
        } else {
            queryDTO.setOnlyOwnRoles(true);
        }
        return queryRolesByCondition(queryDTO);
    }

    @Override
    public List<SysRoleResp> queryAllRoles() {
        // 检查当前用户是否为管理员
        UserDTO currentUser = SecurityUtil.getCurrentUser();
        if (currentUser == null || !currentUser.isManger()) {
            log.warn("非管理员用户尝试查询所有角色，返回空列表");
            return Collections.emptyList();
        }

        // 查询所有角色
        return convertToRespList(sysRoleMapper.selectAll(null));
    }

    @Override
    public List<SysRoleResp> queryOwnRoles() {
        UserDTO currentUser = SecurityUtil.getCurrentUser();
        if (currentUser == null) {
            log.warn("无法获取当前用户信息，返回空角色列表");
            return Collections.emptyList();
        }

        // 获取用户的角色编码列表
        List<String> userRoleCodes = currentUser.getRoles();
        if (userRoleCodes == null || userRoleCodes.isEmpty()) {
            log.info("当前用户没有分配角色，返回空列表");
            return Collections.emptyList();
        }

        // 查询用户拥有的角色
        return convertToRespList(sysRoleMapper.selectAll(userRoleCodes));
    }

    @Override
    public List<SysRoleResp> queryRolesByCondition(RoleQueryDTO queryDTO) {
        if (queryDTO == null) {
            queryDTO = new RoleQueryDTO();
        }

        UserDTO currentUser = SecurityUtil.getCurrentUser();
        List<String> userRoleCodes = currentUser.getRoles();
        // 如果已指定roleCodes，则取交集
        if (queryDTO.getRoleCodes() != null && !queryDTO.getRoleCodes().isEmpty()) {
            userRoleCodes.retainAll(queryDTO.getRoleCodes());
        }
        queryDTO.setRoleCodes(userRoleCodes);
        return convertToRespList(sysRoleMapper.selectAll(queryDTO.getRoleCodes(), queryDTO.getRoleName(), queryDTO.getEnabled()));
    }
    
    @Override
    public PageResult<SysRoleResp> queryRolesByPage(RoleQueryDTO queryDTO) {
        if (queryDTO == null) {
            queryDTO = new RoleQueryDTO();
        }

        UserDTO currentUser = SecurityUtil.getCurrentUser();
        if (currentUser == null) {
            log.warn("无法获取当前用户信息，返回空角色列表");
            return new PageResult<>(Collections.emptyList(), 0, queryDTO.getCurrent(), queryDTO.getSize());
        }
        
        // 根据用户类型决定查询方式
        if (currentUser.isManger()) {
            queryDTO.setOnlyOwnRoles(false);
        } else {
            queryDTO.setOnlyOwnRoles(true);
            List<String> userRoleCodes = currentUser.getRoles();
            // 如果已指定roleCodes，则取交集
            if (queryDTO.getRoleCodes() != null && !queryDTO.getRoleCodes().isEmpty()) {
                userRoleCodes.retainAll(queryDTO.getRoleCodes());
            }
            queryDTO.setRoleCodes(userRoleCodes);
        }
        
        // 创建MyBatis-Plus分页对象
        Page<SysRoleEntity> page = new Page<>(queryDTO.getCurrent(), queryDTO.getSize());
        
        // 调用Mapper的分页查询方法
        IPage<SysRoleEntity> resultPage = sysRoleMapper.selectPage(page, queryDTO.getRoleCodes(), 
                queryDTO.getRoleName(), queryDTO.getEnabled());
        
        // 转换结果
        List<SysRoleResp> records = convertToRespList(resultPage.getRecords());
        
        // 构建并返回分页结果
        return new PageResult<>(records, resultPage.getTotal(), resultPage.getCurrent(), resultPage.getSize());
    }

    /**
     * 将实体列表转换为响应对象列表
     */
    private List<SysRoleResp> convertToRespList(List<SysRoleEntity> entityList) {
        if (entityList == null || entityList.isEmpty()) {
            return Collections.emptyList();
        }

        // 将 Entity 列表转换为 DTO 列表
        List<SysRoleDTO> sysRoleDTOList = entityList.stream()
                .map(entity -> SysRoleTransfer.INSTANCE.toSysRoleDTO(entity))
                .collect(Collectors.toList());

        // 将 DTO 列表转换为 Resp 列表
        return sysRoleDTOList.stream()
                .map(sysRoleDTO -> SysRoleTransfer.INSTANCE.toSysRoleResp(sysRoleDTO))
                .collect(Collectors.toList());
    }
}
