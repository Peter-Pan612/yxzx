package com.yxzx.service;

import com.github.pagehelper.PageInfo;
import com.yxzx.model.dto.system.SysRoleDto;
import com.yxzx.model.entity.system.SysRole;

import java.util.Map;

public interface SysRoleService {
    PageInfo<SysRole> findByPage(SysRoleDto sysRoleDto, Integer current, Integer limit);

    void saveSysRole(SysRole sysRole);

    void updateSysRole(SysRole sysRole);

    void deleteById(Long roleId);

    Map<String, Object> findAll(Long userId);
}
