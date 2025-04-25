package com.example.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.dal.entity.SysUserEntity;
import com.example.dal.mapper.SysUserMapper;
import com.example.service.dto.UserDTO;
import com.example.web.mapper.UserTransfer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.Set;

@Service
public class SysUserService {
    @Autowired
    private SysUserMapper sysUserMapper;

    /**
     * 根据用户名获取用户信息
     *
     * @param username 用户名
     * @return .
     */
    public Optional<SysUserEntity> loadUserByUsername(String username) {
        //LambdaQueryWrapper<SysUser> queryWrapper = Wrappers.lambdaQuery(SysUser.class)
        //        .eq(SysUser::getUsername, username);
        //return Optional.ofNullable(getOne(queryWrapper));
        SysUserEntity sysUser = sysUserMapper.getByUsername(username);
        return Optional.ofNullable(sysUser);
    }

    /**
     * 根据用户名获取角色编码
     *
     * @return .
     */
    public Set<String> getRoleCodeByUsername(String username) {
        return sysUserMapper.getRoleCodeByUsername(username);
    }

    public Set<String> queryRoleIdsByUserId(String id) {
        return sysUserMapper.queryRoleIdsByUserId(id);
    }

    /**
     * 获取所有角色编码
     *
     * @return .
     */
    public Set<String> allRoleCode() {
        return sysUserMapper.allRoleCode();
    }

    // 修改方法，返回 IPage<UserDTO>
    public IPage<UserDTO> queryUserPage(int size, int current) {
        // 校验参数
        if (current <= 0) {
            current = 1;
        }
        if (size <= 0) {
            size = 10;
        }

        // 1. 创建 MP 的 Page 对象
        IPage<SysUserEntity> pageRequest = new Page<>(current, size);

        // 2. 调用 Mapper 方法，传入 Page 对象 (不再需要手动计算 offset)
        // MP分页插件会自动拦截这个调用，并附加分页SQL以及执行Count查询
        IPage<SysUserEntity> userEntityPage = sysUserMapper.selectUserPage(pageRequest);

        // 3. 将 IPage<SysUserEntity> 转换为 IPage<UserDTO>
        IPage<UserDTO> userDtoPage = userEntityPage.convert(entity -> {
            return UserTransfer.INSTANCE.toUserDto(entity);
        });

        return userDtoPage;
    }

    // 原来的 queryUserList 方法可以修改或删除
    // public List<UserDTO> queryUserList(int size, int current) { ... }
}
