package com.yxzx.controller;

import com.yxzx.model.dto.system.AssginMenuDto;
import com.yxzx.model.vo.common.Result;
import com.yxzx.model.vo.common.ResultCodeEnum;
import com.yxzx.service.SysRoleMenuService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * @author: Peter
 * @description:
 * @date: 2024/2/7 19:40
 */
@RestController
@RequestMapping(value = "/admin/system/sysRoleMenu")
public class SysRoleMenuController {
    @Autowired
    private SysRoleMenuService sysRoleMenuService;

    //1 查询所有菜单 和 查询角色分配过菜单id列表
    @GetMapping(value = "/findSysRoleMenuByRoleId/{roleId}")
    public Result findSysRoleMenuByRoleId(@PathVariable("roleId") Long roleId) {
        Map<String, Object> map = sysRoleMenuService.findSysRoleMenuByRoleId(roleId);
        return Result.build(map, ResultCodeEnum.SUCCESS);
    }


    //2 保存角色分配菜单数据
    @PostMapping("/doAssign")
    public Result doAssign(@RequestBody AssginMenuDto assginMenuDto) {

        //保存分配数据
        List<Map<String, Number>> menuInfo = assginMenuDto.getMenuIdList();
        if (menuInfo != null && menuInfo.size() > 0) { //角色分配了菜单
            sysRoleMenuService.doAssign(assginMenuDto);
        }
        return Result.build(null, ResultCodeEnum.SUCCESS);
    }

}
