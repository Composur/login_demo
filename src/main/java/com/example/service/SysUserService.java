package com.example.service;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.common.Response;
import com.example.common.util.PasswordUtil;
import com.example.dal.entity.SysUserEntity;
import com.example.dal.mapper.SysUserMapper;
import com.example.web.mapper.UserTransfer;
import com.example.web.req.PwdRestReq;
import com.example.web.req.UserQueryReq;
import com.example.web.req.UserSaveReq;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Service
public class SysUserService extends ServiceImpl<SysUserMapper, SysUserEntity> {
    // @Autowired // 如果继承了 ServiceImpl，这个可以移除，使用 baseMapper
    //private SysUserMapper baseMapper;


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
        // 5. 设置其他必要字段 (审计字段将自动填充)
        // sysUser.setCreatedBy(SecurityUtil.getCurrentUsername()); // 移除
        // sysUser.setModified(LocalDateTime.now()); // 移除 (注意：created 字段也应在实体中标记为 INSERT fill)
        // sysUser.setModifiedBy(SecurityUtil.getCurrentUsername()); // 移除

        // 6. 调用 Mapper 执行插入操作
        // 如果您继承了 ServiceImpl，可以直接调用 this.save(sysUser) 或 baseMapper.insert(sysUser)
        boolean saved = this.save(sysUser); // ServiceImpl 的 save 方法
        if (!saved) {
            return Response.error("保存失败");
        }
        List<String> roleIds = req.getRoleIds();
        // 确保 sysUser.getId() 在 save 方法后能获取到
        if (sysUser.getId() != null && roleIds != null && !roleIds.isEmpty()) {
            baseMapper.saveUserRole(sysUser.getId(), new HashSet<>(roleIds), sysUser.getId());
        }
        return Response.success("保存成功");
    }

    /**
     * 更新用户
     *
     * @param req .
     * @return .
     */
    @Transactional
    public Response<?> update(UserSaveReq req) {
        // 1. 根据 ID 查询用户
        SysUserEntity sysUser = baseMapper.selectById(req.getId()); // 使用 baseMapper
        if (sysUser == null) {
            return Response.error("用户不存在");
        }

        // 2. 更新用户信息
        UserTransfer.INSTANCE.updateUserFromReq(req, sysUser);

        // 3. 特殊字段处理 (例如密码加密)
        if (StrUtil.isNotBlank(req.getPassword())) {
            if (StrUtil.isBlank(req.getConfirmPassword()) || !req.getPassword().equals(req.getConfirmPassword())) {
                return Response.error("二次密码不一致");
            }
            sysUser.setPassword(PasswordUtil.encoder(req.getPassword()));
        } else {
            // 如果密码为空，确保不更新密码字段，或者在 UserTransfer 中处理
            sysUser.setPassword(null); // 这样 updateById 时，如果密码为 null，MP 默认不会更新该字段
        }


        // 4. 持久化更新到数据库
        boolean updated = this.updateById(sysUser); // ServiceImpl 的 updateById 方法
        if (!updated) {
            return Response.error("更新用户信息失败，可能没有数据被修改或用户不存在");
        }

        // 5. 更新用户角色关联
        if (req.getRoleIds() != null) {
            baseMapper.deleteUserRoleByUserId(sysUser.getId());
            if (!req.getRoleIds().isEmpty()) {
                //String s = null;
                //s.toString();
                baseMapper.saveUserRole(sysUser.getId(), new HashSet<>(req.getRoleIds()), sysUser.getId());
            }
        }

        return Response.success("更新成功");
    }

    /**
     * 重置密码
     *
     * @param username 用户ID.
     * @param req      包含新密码和确认密码的请求对象.
     * @return 操作是否成功.
     */
    public boolean resetPasswd(String username, PwdRestReq req) {
        // 1. 根据 ID 查询用户
        SysUserEntity sysUserEntity = baseMapper.getByUsername(username);
        if (sysUserEntity == null) {
            return false;
        }


        // 3. 对新密码进行加密
        String encodedPassword = PasswordUtil.encoder(req.getNewPassword());
        sysUserEntity.setPassword(encodedPassword);

        // 4. 更新用户信息
        // setModifiedBy 和 setModified 会由 MybatisPlus 自动填充逻辑处理 (如果配置了)
        return this.updateById(sysUserEntity); // 使用 ServiceImpl 的 updateById 方法
    }

    /**
     * 删除用户
     *
     * @param ids .
     * @return .
     */
    public boolean deleteByIds(Set<String> ids) {
        //return baseMapper.deleteBatchIds(ids);
        return removeByIds(ids);
    }


    /**
     * 根据用户名查询用户是否存在
     **/
    public boolean checkUsername(String username) {
        // 调用 Mapper 层获取用户数量
        Integer count = baseMapper.checkUsername(username);
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
        SysUserEntity sysUser = baseMapper.getByUsername(username);
        return Optional.ofNullable(sysUser);
    }

    /**
     * 根据用户名获取角色编码
     *
     * @return .
     */
    public Set<String> getRoleCodeByUsername(String username) {
        return baseMapper.getRoleCodeByUsername(username);
    }


    /**
     * 获取所有角色编码
     *
     * @return .
     */
    public Set<String> allRoleCode() {
        return baseMapper.allRoleCode();
    }

    // 修改方法，返回 IPage<UserDTO>
    public IPage<SysUserEntity> queryUserPage(UserQueryReq queryReq) {
        // 校验参数
        int current = queryReq.getCurrent();
        int size = queryReq.getSize();

        if (current <= 0) {
            current = 1;
        }
        if (size <= 0) {
            size = 10;
        }

        // 1. 创建 MP 的 Page 对象
        IPage<SysUserEntity> pageRequest = new Page<>(current, size);

        // 2. 调用 Mapper 方法，传入 Page 对象和从 UserQueryReq 中获取的查询参数
        IPage<SysUserEntity> userEntityPage = baseMapper.selectUserPage(
                pageRequest,
                queryReq
        );

        // 3. 直接返回 IPage<SysUserEntity>
        return userEntityPage;
    }

    /**
     * 根据用户ID查询角色ID列表
     *
     * @param id 用户ID
     * @return 角色ID列表
     */
    public Set<String> queryRoleIdsByUserId(String id) {
        return baseMapper.queryRoleIdsByUserId(id);
    }
}
