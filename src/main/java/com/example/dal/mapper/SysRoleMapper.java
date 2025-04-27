package com.example.dal.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.dal.entity.SysRoleEntity;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param; // 引入 Param

import java.util.List;

@Mapper
public interface SysRoleMapper extends BaseMapper<SysRoleEntity> {

    // 修改 selectAll 方法签名，接受 roleCodes 参数
    List<SysRoleEntity> selectAll(@Param("roleCodes") List<String> roleCodes);

    // 删除或注释掉 selectRolesByCodes 的定义
    // List<SysRoleEntity> selectRolesByCodes(@Param("roleCodes") List<String> roleCodes);

}
