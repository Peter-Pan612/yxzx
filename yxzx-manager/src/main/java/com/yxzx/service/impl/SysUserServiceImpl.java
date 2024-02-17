package com.yxzx.service.impl;

import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson.JSON;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.yxzx.commom.log.annotation.Log;
import com.yxzx.exception.yxzxException;
import com.yxzx.mapper.SysMenuMapper;
import com.yxzx.mapper.SysRoleUserMapper;
import com.yxzx.mapper.SysUserMapper;
import com.yxzx.model.dto.system.AssginRoleDto;
import com.yxzx.model.dto.system.LoginDto;
import com.yxzx.model.dto.system.SysUserDto;
import com.yxzx.model.entity.system.SysMenu;
import com.yxzx.model.entity.system.SysUser;
import com.yxzx.model.vo.common.ResultCodeEnum;
import com.yxzx.model.vo.system.LoginVo;
import com.yxzx.model.vo.system.SysMenuVo;
import com.yxzx.service.SysUserService;
import com.yxzx.utils.AuthContextUtil;
import com.yxzx.utils.MenuHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.util.DigestUtils;

import java.util.LinkedList;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

/**
 * @author: Peter
 * @description:
 * @date: 2024/1/27 20:45
 */

@Service
public class SysUserServiceImpl implements SysUserService {

    @Autowired
    private SysUserMapper sysUserMapper;

    @Autowired
    private SysRoleUserMapper sysRoleUserMapper;


    @Autowired
    private SysMenuMapper sysMenuMapper;

    @Autowired
    private RedisTemplate<String, String> redisTemplate;

    @Override
    public LoginVo login(LoginDto loginDto) {
        //获取输入验证码和存储到redis的key名称 loginDto获取到
        String captcha = loginDto.getCaptcha(); //用户输入的验证码
        String key = loginDto.getCodeKey();

        //2 根据获取的redis里面的key，查询redis里面存储验证码
        String redisCode = redisTemplate.opsForValue().get("user:validate" + key);
        //3 比较输入的验证码和redis存储验证码是否一致
        if (StrUtil.isEmpty(redisCode) || !StrUtil.equalsIgnoreCase(redisCode, captcha)) {
            //4 如果不一致，提示用户，校验失败
            throw new yxzxException(ResultCodeEnum.VALIDATECODE_ERROR);
        }


        //5 如果一致，删除redis里面验证码
        redisTemplate.delete("user:validate" + key);


        //1 H获取提交用户名loginDto 获取到
        String userName = loginDto.getUserName();

        //2 根据用户名查询数据库表sys_user
        SysUser sysUser = sysUserMapper.selectUserInfoByUserName(userName);
        //3 如果根据用户名查不到对应信息，用户不存在，返回错误信息
        if (sysUser == null) {
//            throw new RuntimeException("用户名不存在");
            throw new yxzxException(ResultCodeEnum.LOGIN_ERROR);
        }

        //4 如果根据用户名查询到用户信息，用户存在
        //5 获取输入的密码，比较输入的密码和数据库密码是否一致
        String database_password = sysUser.getPassword();
        //把输入的密码进行加密，再比较数据库密码,md5
        String input_password = DigestUtils.md5DigestAsHex(loginDto.getPassword().getBytes());
        //6 如果密码一致，登录成功，如果密码不一致登录失败
        //比较
        if (!input_password.equals(database_password)) {
            throw new yxzxException(ResultCodeEnum.LOGIN_ERROR);
        }


        //7 登录成功，生成用户唯一表示token
        String token = UUID.randomUUID().toString().replaceAll("-", "");
        //8 把登录成功用户信息放到redis里面
        //key: token   value；用户信息
        redisTemplate.opsForValue().set("user:login" + token, JSON.toJSONString(sysUser), 7, TimeUnit.DAYS);

        //9 返回loginVo对象
        LoginVo loginVo = new LoginVo();
        loginVo.setToken(token);

        return loginVo;
    }

    //获取当前登录用户信息
    @Override
    public SysUser getUserInfo(String token) {
        String userJson = redisTemplate.opsForValue().get("user:login" + token);
        System.out.println("userJson:" + userJson);
        SysUser sysUser = JSON.parseObject(userJson, SysUser.class);

        return sysUser;
    }

    //退出登录
    @Override
    public void logout(String token) {

        redisTemplate.delete("user:login" + token);
    }

    // 用户条件分页查询接口
    @Override
    public PageInfo<SysUser> findByPage(Integer pageNum, Integer pageSize, SysUserDto sysUserDto) {
        PageHelper.startPage(pageNum, pageSize);
        List<SysUser> list = sysUserMapper.findByPage(sysUserDto);
        PageInfo<SysUser> pageInfo = new PageInfo<>(list);

        return pageInfo;
    }

    //用户添加
    @Override
    public void saveSysUser(SysUser sysUser) {
        //1 判断用户名不能重复
        String userName = sysUser.getUserName();
        SysUser dbSysUser = sysUserMapper.selectUserInfoByUserName(userName);
        if (dbSysUser != null) {
            throw new yxzxException(ResultCodeEnum.USER_NAME_IS_EXISTS);
        }

        //设置status值 1可用 0 不可用
        sysUser.setStatus(1);

        //2 输入密码进行加密
        String md5_password = DigestUtils.md5DigestAsHex(sysUser.getPassword().getBytes());
        sysUser.setPassword(md5_password);
        sysUserMapper.save(sysUser);
    }

    //用户修改
    @Override
    public void updateSysUser(SysUser sysUser) {
        sysUserMapper.update(sysUser);
    }


    //用户删除
    @Override
    public void deleteById(Long userId) {
        sysUserMapper.delete(userId);
    }


    //用户分配角色
    @Log(title = "用户分配角色", businessType = 0)
    @Transactional
    @Override
    public void doAssign(AssginRoleDto assginRoleDto) {
        //1 根据userid删除用户之前分配角色数据
        sysRoleUserMapper.deleteByUserId(assginRoleDto.getUserId());

        //TODO 为了测试，模拟异常
        //int a = 1 / 0;

        //2 重新分配
        List<Long> roleIdList = assginRoleDto.getRoleIdList();
        //遍历得到每个角色id
        for (Long roleId : roleIdList) {
            sysRoleUserMapper.doAssign(assginRoleDto.getUserId(), roleId);

        }
    }

    //查询用户可以操作菜单
    @Override
    public List<SysMenuVo> findMenusByUserId() {
        //获取当前用户id
        SysUser sysUser = AuthContextUtil.get();
        Long userId = sysUser.getId();

        //根据userId查询可以操作菜单
        //封装要求数据格式，返回
        List<SysMenu> sysMenuList = MenuHelper.buildTree(sysMenuMapper.findMenusByUserId(userId));

        List<SysMenuVo> sysMenuVos = this.buildMenus(sysMenuList);

        return sysMenuVos;
    }

    // 将List<SysMenu>对象转换成List<SysMenuVo>对象
    private List<SysMenuVo> buildMenus(List<SysMenu> menus) {

        List<SysMenuVo> sysMenuVoList = new LinkedList<SysMenuVo>();
        for (SysMenu sysMenu : menus) {
            SysMenuVo sysMenuVo = new SysMenuVo();
            sysMenuVo.setTitle(sysMenu.getTitle());
            sysMenuVo.setName(sysMenu.getComponent());
            List<SysMenu> children = sysMenu.getChildren();
            if (!CollectionUtils.isEmpty(children)) {
                sysMenuVo.setChildren(buildMenus(children));
            }
            sysMenuVoList.add(sysMenuVo);
        }
        return sysMenuVoList;
    }


}
