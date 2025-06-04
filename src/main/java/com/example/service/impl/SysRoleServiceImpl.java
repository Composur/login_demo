package com.example.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.dal.entity.SysRoleEntity;
import com.example.dal.mapper.SysRoleMapper;
import com.example.security.utils.SecurityUtil;
import com.example.service.SysPermissionService;
import com.example.service.SysRoleService;
import com.example.service.dto.RoleQueryDTO;
import com.example.service.dto.SysRoleDTO;
import com.example.service.dto.UserDTO;
import com.example.web.mapper.SysRoleTransfer;
import com.example.web.req.SysRoleSaveReq;
import com.example.web.resp.PageResult;
import com.example.web.resp.SysRoleResp;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
@Slf4j
public class SysRoleServiceImpl implements SysRoleService {

    private final SysRoleMapper sysRoleMapper;
    private final SysPermissionService sysPermissionService; // 注入SysPermissionService


    /**
     * 保存系统角色信息
     *
     * @param req 系统角色保存请求对象，包含需要保存的角色信息
     * @return 返回保存后的角色ID，如果保存失败则返回空字符串
     */
    @Override
    public String save(SysRoleSaveReq req) {
        // 将系统角色保存请求对象转换为系统角色实体对象
        SysRoleEntity entity = SysRoleTransfer.INSTANCE.toSysRoleEntity(req);
        // 插入系统角色实体对象到数据库
        sysRoleMapper.insert(entity);
        // 检查插入后的实体对象ID是否为空，以判断保存是否成功
        if (entity.getId() == null) {
            // 如果角色ID为空，则记录错误日志并返回空字符串
            log.error("保存角色失败，角色ID为空");
            return "";
        }
        return entity.getId();
    }

    /**
     * 更新系统角色信息
     *
     * @param id  系统角色ID
     * @param req 系统角色保存请求对象，包含需要更新的角色信息
     * @return 返回更新后的角色ID，如果更新失败则返回空字符串
     */
    @Override
    public String update(String id, SysRoleSaveReq req) {
        if (id == null) {
            log.error("角色ID为空，更新失败");
            throw new IllegalArgumentException("角色ID不能为空");
        }
        if (req == null) {
            log.error("角色更新请求为空");
            throw new IllegalArgumentException("请求参数不能为空");
        }

        SysRoleEntity entity = SysRoleTransfer.INSTANCE.toSysRoleEntity(req);
        entity.setId(id);
        int rowsUpdated = sysRoleMapper.updateById(entity);
        if (rowsUpdated == 0) {
            log.warn("未找到要更新的角色，ID: {}", id);
        }
        return entity.getId();
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
     * 检查角色编码是否存在
     */
    @Override
    public Boolean checkCode(String code) {
        QueryWrapper<SysRoleEntity> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("role_code", code);
        return sysRoleMapper.selectCount(queryWrapper) > 0;
    }

    /**
     * 删除角色
     */
    @Override
    public String delete(String id) {
        if (id == null || id.trim().isEmpty()) {
            log.error("删除角色失败，角色ID为空");
            throw new IllegalArgumentException("角色ID不能为空");
        }
        int rowsDeleted = sysRoleMapper.deleteById(id);
        if (rowsDeleted > 0) {
            log.info("成功删除角色，ID: {}", id);
        } else {
            log.warn("删除角色失败或角色不存在，ID: {}", id);
            // 根据业务需求，如果删除不成功（例如，角色不存在），您可能希望抛出异常或返回特定错误代码
            //throw new EntityNotFoundException("未找到要删除的角色，ID: " + id);
        }
        return id;
    }

    /**
     * 查询角色权限
     */
    @Override
    public List<String> queryPermissions(String id) {
        if (id == null || id.isEmpty()) {
            return Collections.emptyList();
        }

        // 将单个角色ID转换为List
        List<String> roleIds = Collections.singletonList(id);

        // 使用SysPermissionService查询角色拥有的权限编码
        Set<String> permissionCodes = sysPermissionService.listPermissionByRoleIds(roleIds);

        // 将Set转换为List返回
        return new ArrayList<>(permissionCodes);
    }

    /**
     * 授予角色权限
     */
    @Override
    public boolean grantPermission(String id, List<String> permissionIds) {
        if (id == null || id.isEmpty()) {
            throw new IllegalArgumentException("角色ID不能为空");
        }
        if (permissionIds != null && !permissionIds.isEmpty()) {
            return sysPermissionService.grantPermission(id, permissionIds);
        }
        log.warn("无效的权限ID列表: {} 角色ID: {}","null", id);
        throw new IllegalArgumentException("权限ID列表不合法");
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
