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

    public List<UserDTO> queryUserList() {
        // 将变量类型修改为实际的返回类型 List<SysUserEntity>
        List<SysUserEntity> userEntities = sysUserMapper.selectList();

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
