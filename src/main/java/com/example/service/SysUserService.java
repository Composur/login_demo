package com.example.service;

import com.example.dal.entity.SysUserEntity;
import com.example.dal.mapper.SysUserMapper;
import com.example.service.dto.UserDTO;
import com.example.web.mapper.UserTransfer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
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

    public List<UserDTO> queryUserList(int size, int current) {
        // 校验参数 (可选但推荐)
        if (current <= 0) {
            current = 1; // 默认第一页
        }
        if (size <= 0) {
            size = 10; // 默认每页10条
        }

        // 计算 offset (MySQL offset 从 0 开始)
        int offset = (current - 1) * size;

        // 调用 Mapper 方法，传递计算好的 offset 和原始的 size (作为 limit)
        // 注意：需要确保 SysUserMapper 接口中的 selectList 方法参数名与 XML 中的占位符匹配
        // 可能需要使用 @Param("offset") 和 @Param("limit") 注解
        List<SysUserEntity> userEntities = sysUserMapper.selectList(offset, size);

        //return userEntities.stream().map(userEntity -> {
        //    //UserDTO userDTO = new UserDTO();
        //    //userDTO.setId(userEntity.getId());
        //    //userDTO.setUsername(userEntity.getUsername());
        //    //userDTO.setNickname(userEntity.getNickname());
        //    //return userDTO;
        //    return UserTransfer.INSTANCE.toUserDto(userEntity);
        //}).collect(Collectors.toList());
        return UserTransfer.INSTANCE.toUserDtoList(userEntities);
    }

}
