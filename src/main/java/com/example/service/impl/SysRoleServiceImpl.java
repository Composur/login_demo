package com.example.service.impl;

import com.example.dal.entity.SysRoleEntity;
import com.example.dal.mapper.SysRoleMapper;
import com.example.security.utils.SecurityUtil;
import com.example.service.SysRoleService;
import com.example.service.dto.RoleQueryDTO;
import com.example.service.dto.SysRoleDTO;
import com.example.service.dto.UserDTO;
import com.example.web.mapper.SysRoleTransfer;
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
    public List<SysRoleResp> queryList() {
        UserDTO currentUser = SecurityUtil.getCurrentUser();
        
        // 如果用户不存在，返回空列表
        if (currentUser == null) {
            log.warn("无法获取当前用户信息，返回空角色列表");
            return Collections.emptyList();
        }
        
        // 根据用户类型决定查询方式
        if (currentUser.isManger()) {
            return queryAllRoles();
        } else {
            return queryOwnRoles();
        }
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
        
        // 如果用户不存在，返回空列表
        if (currentUser == null) {
            log.warn("无法获取当前用户信息，返回空角色列表");
            return Collections.emptyList();
        }
        
        // 处理只查询自己角色的情况
        if (queryDTO.isOnlyOwnRoles() && !currentUser.isManger()) {
            List<String> userRoleCodes = currentUser.getRoles();
            if (userRoleCodes == null || userRoleCodes.isEmpty()) {
                return Collections.emptyList();
            }
            
            // 如果已指定roleCodes，则取交集
            if (queryDTO.getRoleCodes() != null && !queryDTO.getRoleCodes().isEmpty()) {
                userRoleCodes.retainAll(queryDTO.getRoleCodes());
                if (userRoleCodes.isEmpty()) {
                    return Collections.emptyList();
                }
            }
            
            queryDTO.setRoleCodes(userRoleCodes);
        }
        
        // 非管理员且未指定只查询自己角色时，强制只查询自己的角色
        if (!currentUser.isManger() && !queryDTO.isOnlyOwnRoles()) {
            List<String> userRoleCodes = currentUser.getRoles();
            if (userRoleCodes == null || userRoleCodes.isEmpty()) {
                return Collections.emptyList();
            }
            
            // 如果已指定roleCodes，则取交集
            if (queryDTO.getRoleCodes() != null && !queryDTO.getRoleCodes().isEmpty()) {
                userRoleCodes.retainAll(queryDTO.getRoleCodes());
                if (userRoleCodes.isEmpty()) {
                    return Collections.emptyList();
                }
            }
            
            queryDTO.setRoleCodes(userRoleCodes);
        }
        
        // TODO: 这里应该扩展SysRoleMapper.selectAll方法，支持更多查询条件
        // 目前先简单实现，只按roleCodes查询
        return convertToRespList(sysRoleMapper.selectAll(queryDTO.getRoleCodes()));
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
