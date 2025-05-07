package com.example.service;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.common.Response;
import com.example.common.util.PasswordUtil;
import com.example.dal.entity.SysUserEntity;
import com.example.dal.mapper.SysUserMapper;
import com.example.web.mapper.UserTransfer;
import com.example.web.req.UserSaveReq;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Service
public class SysUserService {
    @Autowired
    private SysUserMapper sysUserMapper;


    /**
     * 保存用户
     *
     * @param req .
     * @return .
     */
    public Response<?> save(UserSaveReq req) {
        // 1. 校验用户名是否已存在
        boolean checkUsername = this.checkUsername(req.getUsername());
        if (checkUsername) return Response.error("用户名已经存在");
        // 2. 校验密码和确认密码是否一致
        if (StrUtil.isNotBlank(req.getPassword()) && !req.getPassword().equals(req.getConfirmPassword()))
            return Response.error("二次密码不一致");
        // 3. 将 UserSaveReq 转换为 SysUserEntity
        SysUserEntity sysUser = UserTransfer.INSTANCE.toSysUserEntity(req);
        // 4. 对密码进行加密
        sysUser.setPassword(PasswordUtil.encoder(sysUser.getPassword()));
        // 5. 设置其他必要字段
        // 6. 调用 Mapper 执行插入操作
        int insertedRows = sysUserMapper.insert(sysUser);
        if (insertedRows <= 0) {
            return Response.error("保存失败");
        }
        List<String> roleIds = req.getRoleIds();
        sysUserMapper.saveUserRole(sysUser.getId(), new HashSet<>(roleIds));
        return Response.success("保存成功");
    }

    /**
     * 根据用户名查询用户是否存在
     **/
    public boolean checkUsername(String username) {
        // 调用 Mapper 层获取用户数量
        Integer count = sysUserMapper.checkUsername(username);
        // 在 Service 层判断数量是否大于 0，返回 boolean 值
        return count > 0;
    }

    ;

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


    /**
     * 获取所有角色编码
     *
     * @return .
     */
    public Set<String> allRoleCode() {
        return sysUserMapper.allRoleCode();
    }

    // 修改方法，返回 IPage<UserDTO>
    public IPage<SysUserEntity> queryUserPage(int size, int current) {
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
        IPage<SysUserEntity> userDtoPage = userEntityPage.convert(entity -> {
            return UserTransfer.INSTANCE.toUserDto(entity);
        });

        return userDtoPage;
    }

    /**
     * 根据用户ID查询角色ID列表
     *
     * @param id 用户ID
     * @return 角色ID列表
     */
    public Set<String> queryRoleIdsByUserId(String id) {
        return sysUserMapper.queryRoleIdsByUserId(id);
    }
}
