package com.example.dal.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.example.dal.entity.SysUserEntity;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.Set;

@Mapper
public interface SysUserMapper extends BaseMapper<SysUserEntity> {
    /**
     * 根据用户名查询用户是否存在
     *
     * @param username .
     * @return .
     */
    Integer checkUsername(@Param("username") String username); // 修改返回类型为 Integer

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
     * 获取所有用户信息 (使用MP分页插件)
     * 第一个参数必须是 IPage 类型
     *
     * @param page 分页参数对象 (包含 current, size)
     * @return 分页结果对象 (包含 records, total, pages 等)
     */
    IPage<SysUserEntity> selectUserPage(IPage<SysUserEntity> page); // 修改方法签名和名称

    // 原来的 selectList 方法可以删掉或保留用于非分页场景
    // List<SysUserEntity> selectList(@Param("offset") int offset, @Param("limit") int limit); // 可以删除或注释掉
}
