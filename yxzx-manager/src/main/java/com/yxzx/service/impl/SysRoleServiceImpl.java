package com.yxzx.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.yxzx.mapper.SysRoleMapper;
import com.yxzx.mapper.SysRoleUserMapper;
import com.yxzx.model.dto.system.SysRoleDto;
import com.yxzx.model.entity.system.SysRole;
import com.yxzx.service.SysRoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author: Peter
 * @description:
 * @date: 2024/2/5 20:21
 */
@Service
public class SysRoleServiceImpl implements SysRoleService {

    @Autowired
    private SysRoleMapper sysRoleMapper;

    @Autowired
    private SysRoleUserMapper sysRoleUserMapper;

    //角色列表的方法
    @Override
    public PageInfo<SysRole> findByPage(SysRoleDto sysRoleDto, Integer current, Integer limit) {
        //设置分页参数
        PageHelper.startPage(current, limit);
        //根据条件查询所有数据
        List<SysRole> list = sysRoleMapper.findByPage(sysRoleDto);
        //封装pageInfo对象
        PageInfo<SysRole> pageInfo = new PageInfo<>(list);
        return pageInfo;
    }

    //角色添加
    @Override
    public void saveSysRole(SysRole sysRole) {
        sysRoleMapper.save(sysRole);
    }

    //角色修改
    @Override
    public void updateSysRole(SysRole sysRole) {
        sysRoleMapper.update(sysRole);
    }

    @Override
    public void deleteById(Long roleId) {
        sysRoleMapper.delete(roleId);
    }

    //查询所有角色
    @Override
    public Map<String, Object> findAll(Long userId) {
        //1 查询所有角色
        List<SysRole> roleList = sysRoleMapper.findAll();

        //2 查询分配过的角色列表
        //根据userId查询用户分配过角色id列表
        List<Long> roleIds = sysRoleUserMapper.selectRoleIdsByUserId(userId);


        Map<String, Object> map = new HashMap<>();
        map.put("allRolesList", roleList);
        map.put("sysUserRoles", roleIds);

        return map;
    }
}
