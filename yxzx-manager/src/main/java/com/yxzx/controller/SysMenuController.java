package com.yxzx.controller;

import com.yxzx.model.entity.system.SysMenu;
import com.yxzx.model.vo.common.Result;
import com.yxzx.model.vo.common.ResultCodeEnum;
import com.yxzx.service.SysMenuService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author: Peter
 * @description:
 * @date: 2024/2/7 17:43
 */
@RestController
@RequestMapping("/admin/system/sysMenu")
public class SysMenuController {

    @Autowired
    private SysMenuService sysMenuService;


    //菜单列表
    @GetMapping("/findNodes")
    public Result findNodes() {
        List<SysMenu> list = sysMenuService.findNodes();
        return Result.build(list, ResultCodeEnum.SUCCESS);
    }

    // 菜单添加
    @PostMapping("/save")
    public Result save(@RequestBody SysMenu sysMenu) {
        sysMenuService.save(sysMenu);
        return Result.build(null, ResultCodeEnum.SUCCESS);
    }

    //菜单修改
    @PutMapping("/update")
    public Result update(@RequestBody SysMenu sysMenu) {
        sysMenuService.update(sysMenu);
        return Result.build(null, ResultCodeEnum.SUCCESS);
    }

    //菜单删除
    @DeleteMapping("/removeById/{id}")
    public Result removeById(@PathVariable("id") Long id) {
        sysMenuService.removeById(id);
        return Result.build(null, ResultCodeEnum.SUCCESS);
    }
}
