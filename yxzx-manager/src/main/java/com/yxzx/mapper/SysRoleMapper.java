package com.yxzx.mapper;

import com.yxzx.model.dto.system.SysRoleDto;
import com.yxzx.model.entity.system.SysRole;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface SysRoleMapper {
    List<SysRole> findByPage(SysRoleDto sysRoleDto);

    void save(SysRole sysRole);

    void update(SysRole sysRole);

    void delete(Long roleId);

    List<SysRole> findAll();
}
