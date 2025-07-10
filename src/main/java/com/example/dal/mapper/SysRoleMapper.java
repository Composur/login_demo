package com.example.dal.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.dal.entity.SysRoleEntity;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface SysRoleMapper extends BaseMapper<SysRoleEntity> {

    /**
     * 根据条件查询角色列表
     *
     * @param roleCodes 角色编码列表，为null则不限制
     * @param roleName  角色名称（模糊查询），为null则不限制
     * @param enabled   是否启用，为null则不限制
     * @return 角色列表
     */
    List<SysRoleEntity> selectAll(
            @Param("roleCodes") List<String> roleCodes,
            @Param("roleName") String roleName,
            @Param("enabled") Integer enabled
    );

    /**
     * 根据角色编码列表查询角色
     * 兼容原有方法，内部调用selectAll
     */
    default List<SysRoleEntity> selectAll(@Param("roleCodes") List<String> roleCodes) {
        return selectAll(roleCodes, null, null);
    }

    /**
     * 分页查询角色列表
     *
     * @param page      分页参数
     * @param roleCodes 角色编码列表，为null则不限制
     * @param roleName  角色名称（模糊查询），为null则不限制
     * @param enabled   是否启用，为null则不限制
     * @return 分页结果
     */
    IPage<SysRoleEntity> selectPage(
            Page<SysRoleEntity> page,
            @Param("roleCodes") List<String> roleCodes,
            @Param("roleName") String roleName,
            @Param("enabled") Integer enabled
    );
}
