package com.yxzx.service;

import com.github.pagehelper.PageInfo;
import com.yxzx.model.dto.system.AssginRoleDto;
import com.yxzx.model.dto.system.LoginDto;
import com.yxzx.model.dto.system.SysUserDto;
import com.yxzx.model.entity.system.SysUser;
import com.yxzx.model.vo.system.LoginVo;
import com.yxzx.model.vo.system.SysMenuVo;

import java.util.List;

public interface SysUserService {
    LoginVo login(LoginDto loginDto);

    SysUser getUserInfo(String token);

    void logout(String token);

    PageInfo<SysUser> findByPage(Integer pageNum, Integer pageSize, SysUserDto sysUserDto);

    void saveSysUser(SysUser sysUser);

    void updateSysUser(SysUser sysUser);

    void deleteById(Long userId);

    void doAssign(AssginRoleDto assginRoleDto);

    List<SysMenuVo> findMenusByUserId();
}
