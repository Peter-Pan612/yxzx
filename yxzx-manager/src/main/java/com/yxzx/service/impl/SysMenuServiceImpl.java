package com.yxzx.service.impl;

import com.yxzx.exception.yxzxException;
import com.yxzx.mapper.SysMenuMapper;
import com.yxzx.mapper.SysRoleMenuMapper;
import com.yxzx.model.entity.system.SysMenu;
import com.yxzx.model.vo.common.ResultCodeEnum;
import com.yxzx.service.SysMenuService;
import com.yxzx.utils.MenuHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.List;

/**
 * @author: Peter
 * @description:
 * @date: 2024/2/7 17:44
 */
@Service
public class SysMenuServiceImpl implements SysMenuService {

    @Autowired
    private SysMenuMapper sysMenuMapper;

    @Autowired
    private SysRoleMenuMapper sysRoleMenuMapper;


    //菜单列表
    @Override
    public List<SysMenu> findNodes() {
        //1 查询所有菜单，返回list集合
        List<SysMenu> sysMenuList = sysMenuMapper.findAll();
        if (CollectionUtils.isEmpty(sysMenuList)) {
            return null;
        }

        //2 调用工具类的方法，把返回List集合封装要求数据格式
        List<SysMenu> treeList = MenuHelper.buildTree(sysMenuList);

        return treeList;
    }

    //菜单添加
    @Override
    public void save(SysMenu sysMenu) {
        sysMenuMapper.save(sysMenu);

        //新添加子菜单，把父菜单isHalf半开状态 1
        updateSysRoleMenu(sysMenu);
    }

    //新添加子菜单，把父菜单isHalf半开状态 1
    private void updateSysRoleMenu(SysMenu sysMenu) {
        //获取当前添加菜单的父菜单
        SysMenu parentMenu = sysMenuMapper.selectParentMenu(sysMenu.getParentId());
        if (parentMenu != null) {
            //把父菜单isHalf半开状态 1
            sysRoleMenuMapper.updateSysRoleMenuIsHalf(parentMenu.getId());

            //递归调用
            updateSysRoleMenu(parentMenu);

        }
    }

    //菜单修改
    @Override
    public void update(SysMenu sysMenu) {
        sysMenuMapper.update(sysMenu);
    }

    //菜单删除
    @Override
    public void removeById(Long id) {
        //根据当前菜单id，查询是否包含子菜单

        int count = sysMenuMapper.selectCountById(id);

        //判断，count>0,包含子菜单
        if (count > 0) {
            throw new yxzxException(ResultCodeEnum.NODE_ERROR);
        }

        //count == 0 直接删除
        sysMenuMapper.delete(id);

    }
}
