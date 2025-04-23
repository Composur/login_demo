package com.example.dal.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.dal.entity.SysUserEntity;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Set;

@Mapper
public interface SysUserMapper extends BaseMapper<SysUserEntity> {
    /**
     * 根据用户名获取用户信息
     *
     * @param username .
     * @return .
     */
    SysUserEntity getByUsername(@Param("username") String username);

    /**
     * 根据用户名获取用户信息
     *
     * @return .
     */
    Set<String> getRoleCodeByUsername(@Param("username") String username);

    /**
     * 根据用户id获取角色id
     *
     * @return .
     */
    Set<String> queryRoleIdsByUserId(@Param("userId") String userId);

    /**
     * 获取所有角色编码
     *
     * @return .
     */
    Set<String> allRoleCode();

    /**
     * 获取所有用户信息
     *
     * @return .
     */
    List<SysUserEntity> selectList(@Param("offset") int offset, @Param("size") int limit);
}
