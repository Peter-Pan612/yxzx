package com.yxzx.controller;

import cn.hutool.http.server.HttpServerRequest;
import com.yxzx.model.dto.system.LoginDto;
import com.yxzx.model.entity.system.SysUser;
import com.yxzx.model.vo.common.Result;
import com.yxzx.model.vo.common.ResultCodeEnum;
import com.yxzx.model.vo.system.LoginVo;
import com.yxzx.model.vo.system.SysMenuVo;
import com.yxzx.model.vo.system.ValidateCodeVo;
import com.yxzx.service.SysMenuService;
import com.yxzx.service.SysUserService;
import com.yxzx.service.ValidateCodeService;
import com.yxzx.utils.AuthContextUtil;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author: Peter
 * @description:
 * @date: 2024/1/27 20:43
 */

@Tag(name = "用户接口")
@RestController
@RequestMapping(value = "/admin/system/index")
public class IndexController {

    @Autowired
    private SysUserService sysUserService;

    @Autowired
    private ValidateCodeService validateCodeService;

    @Autowired
    private SysMenuService sysMenuService;

    //用户登录
    @Operation(summary = "登录的方法")
    @PostMapping("/login")
    public Result login(@RequestBody LoginDto loginDto) {
        LoginVo loginVo = sysUserService.login(loginDto);
        return Result.build(loginVo, ResultCodeEnum.SUCCESS);
    }


    //生成图片验证码
    @GetMapping("/generateValidateCode")
    public Result<ValidateCodeVo> generateValidateCode() {
        ValidateCodeVo validateCodeVo = validateCodeService.generateValidateCode();
        return Result.build(validateCodeVo, ResultCodeEnum.SUCCESS);
    }

    //获取当前登录用户信息 v2
    @GetMapping(value = "/getUserInfo")
    public Result getUserInfo() {
        return Result.build(AuthContextUtil.get(), ResultCodeEnum.SUCCESS);
    }

    //获取当前登录用户信息 v1
   /* @GetMapping(value = "/getUserInfo")
    public Result getUserInfo(@RequestHeader(name = "token") String token){
        //1 从请求头获取token

        //2 根据token查询redis获取用户信息
       SysUser sysUser =  sysUserService.getUserInfo(token);

        //3 用户信息返回
        return Result.build(sysUser,ResultCodeEnum.SUCCESS);
    }*/

    //退出登录
    @GetMapping(value = "/logout")
    public Result logout(@RequestHeader(name = "token") String token) {
        sysUserService.logout(token);
        return Result.build(null, ResultCodeEnum.SUCCESS);
    }


    //查询用户可以操作菜单
    @GetMapping("/menus")
    public Result menus() {
        List<SysMenuVo> list = sysUserService.findMenusByUserId();
        return Result.build(list, ResultCodeEnum.SUCCESS);
    }

}
