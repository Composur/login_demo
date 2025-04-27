package com.example.service.impl;

import com.example.dal.entity.SysRoleEntity;
import com.example.dal.mapper.SysRoleMapper;
import com.example.security.utils.SecurityUtil;
import com.example.service.SysRoleService;
import com.example.service.dto.SysRoleDTO;
import com.example.service.dto.UserDTO;
import com.example.web.mapper.SysRoleTransfer;
import com.example.web.resp.SysRoleResp;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class SysRoleServiceImpl implements SysRoleService {

    private final SysRoleMapper sysRoleMapper;

    // 注入 DTO 和 Entity 之间的转换器 (假设你也有一个)
    // @Autowired
    // private YourEntityDtoMapper entityDtoMapper;

    @Override
    public String save() { // 调整了 save 方法签名以匹配接口
        // 实现保存逻辑...
        return "1"; // 示例返回
    }

    @Override // 确保实现了接口方法
    public List<SysRoleResp> queryList() {
        UserDTO currentUser = SecurityUtil.getCurrentUser();
        List<SysRoleEntity> entityList;
        List<String> roleCodesToQuery = null; // 默认为 null，表示查询所有
        // TODO 需要优化
        if (currentUser != null && !currentUser.isManger()) {
            // 如果不是管理员，获取用户的角色编码列表
            if (currentUser.getRoles() != null && !currentUser.getRoles().isEmpty()) {
                roleCodesToQuery = currentUser.getRoles();
                System.out.println("普通用户查询指定角色: " + roleCodesToQuery);
            } else {
                // 用户没有角色，返回空列表
                System.out.println("普通用户无角色，返回空列表");
                entityList = Collections.emptyList();
                // 直接返回，避免后续转换
                return Collections.emptyList();
            }
        } else if (currentUser != null && currentUser.isManger()) {
            // 管理员查询所有角色 (roleCodesToQuery 保持 null)
            System.out.println("管理员查询所有角色");
        } else {
            // 未登录或无法获取用户信息
            System.out.println("无法获取当前用户信息，返回空列表");
            entityList = Collections.emptyList();
            return Collections.emptyList();
        }

        // 调用统一的 selectAll 方法，传入 roleCodesToQuery (管理员时为 null)
        entityList = sysRoleMapper.selectAll(roleCodesToQuery);


        // --- 后续转换逻辑保持不变 ---
        // 2. 将 Entity 列表转换为 DTO 列表
        List<SysRoleDTO> sysRoleDTOList = entityList.stream()
                .map(entity -> SysRoleTransfer.INSTANCE.toSysRoleDTO(entity))
                .collect(Collectors.toList());

        // 3. 将 DTO 列表转换为 Resp 列表
        List<SysRoleResp> sysRoleRespList = sysRoleDTOList.stream()
                .map(sysRoleDTO -> SysRoleTransfer.INSTANCE.toSysRoleResp(sysRoleDTO))
                .collect(Collectors.toList());

        return sysRoleRespList;
    }
}
