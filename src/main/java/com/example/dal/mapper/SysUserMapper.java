package com.example.dal.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.example.dal.entity.SysUserEntity;
import com.example.web.req.PwdRestReq;
import com.example.web.req.UserQueryReq;
import org.apache.ibatis.annotations.Param;

import java.util.Set;

public interface SysUserMapper extends BaseMapper<SysUserEntity> {
    /**
     * 根据用户id删除用户角色关系
     *
     * @param id .
     * @return .
     */
    int deleteUserRoleByUserId(@Param("id") String id);

    /**
     * 根据用户id获取用户信息
     *
     * @param id .
     * @return .
     */
    SysUserEntity selectById(@Param("id") String id);

    /**
     * 保存用户角色
     *
     * @param id      用户id
     * @param roleIds 角色id集合
     * @return .
     */
    void saveUserRole(@Param("id") String id, @Param("roleIds") Set<String> roleIds, @Param("operatorId") String operatorId);

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
    IPage<SysUserEntity> selectUserPage(IPage<SysUserEntity> page, @Param("query") UserQueryReq query); // 修改方法签名和名称

    boolean resetPasswd(String id, PwdRestReq req);
}
