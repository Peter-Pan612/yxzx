package com.yxzx.service.impl;

import com.alibaba.fastjson.JSON;
import com.yxzx.exception.yxzxException;
import com.yxzx.mapper.UserInfoMapper;
import com.yxzx.model.dto.h5.UserLoginDto;
import com.yxzx.model.dto.h5.UserRegisterDto;
import com.yxzx.model.entity.user.UserInfo;
import com.yxzx.model.vo.common.ResultCodeEnum;
import com.yxzx.model.vo.h5.UserInfoVo;
import com.yxzx.service.UserInfoService;
import com.yxzx.utils.AuthContextUtil;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;
import org.springframework.util.StringUtils;

import java.util.UUID;
import java.util.concurrent.TimeUnit;

/**
 * @author: Peter
 * @description:
 * @date: 2024/2/17 15:25
 */
@Service
public class UserInfoServiceImpl implements UserInfoService {
    @Autowired
    private UserInfoMapper userInfoMapper;

    @Autowired
    private RedisTemplate<String, String> redisTemplate;

    //注册
    @Override
    public void register(UserRegisterDto userRegisterDto) {
        //1 获取数据
        String username = userRegisterDto.getUsername();
        String password = userRegisterDto.getPassword();
        String nickName = userRegisterDto.getNickName();
        String code = userRegisterDto.getCode();

        //2 验证码校验
        //从redis获取发送验证码
        String redisCode = redisTemplate.opsForValue().get(username);

        // 获取输入的验证码，进行比对
        if (!redisCode.equals(code)) {
            throw new yxzxException(ResultCodeEnum.VALIDATECODE_ERROR);
        }

        //3 校验用户名不能重复
        UserInfo userInfo = userInfoMapper.selectByUserName(username);
        if (userInfo != null) {//存在相同用户名
            throw new yxzxException(ResultCodeEnum.USER_NAME_IS_EXISTS);
        }

        //4 封装数据，调用方法添加到数据库
        userInfo = new UserInfo();
        userInfo.setUsername(username);
        userInfo.setNickName(nickName);
        userInfo.setPassword(DigestUtils.md5DigestAsHex(password.getBytes()));
        userInfo.setPhone(username);
        userInfo.setStatus(1);
        userInfo.setSex(0);
        userInfo.setAvatar("http://thirdwx.qlogo.cn/mmopen/vi_32/DYAIOgq83eoj0hHXhgJNOTSOFsS4uZs8x1ConecaVOB8eIl115xmJZcT4oCicvia7wMEufibKtTLqiaJeanU2Lpg3w/132");

        userInfoMapper.save(userInfo);

        //5 从redis删除验证码
        redisTemplate.delete(username);

    }

    @Override
    public String login(UserLoginDto userLoginDto) {
        //1 获取用户名和密码
        String username = userLoginDto.getUsername();
        String password = userLoginDto.getPassword();

        //2查询数据库得到信息
        UserInfo userInfo = userInfoMapper.selectByUserName(username);

        //3 比较信息
        String database_password = userInfo.getPassword();
        String md5_password = DigestUtils.md5DigestAsHex(password.getBytes());
        if (!database_password.equals(md5_password)) {
            throw new yxzxException(ResultCodeEnum.LOGIN_ERROR);
        }

        //4 校验用户是否禁用
        if (userInfo.getStatus() == 0) {
            throw new yxzxException(ResultCodeEnum.ACCOUNT_STOP);
        }

        //5 生成token
        String token = UUID.randomUUID().toString().replaceAll("-", "");

        //6用户信息放到redis里面

        redisTemplate.opsForValue().set("user:yxzx:" + token, JSON.toJSONString(userInfo),
                30, TimeUnit.DAYS);

        //7 返回token
        return token;
    }


    @Override
    public UserInfoVo getCurrentUserInfo(String token) {
        //从redis根据token获取用户信息
        /*String userJson = redisTemplate.opsForValue().get("user:yxzx:" + token);
        if (!StringUtils.hasText(userJson)) {
            throw new yxzxException(ResultCodeEnum.LOGIN_ERROR);
        }
        UserInfo userInfo = JSON.parseObject(userJson, UserInfo.class);*/

        //从threadlocal获取当前用户信息
        UserInfo userInfo = AuthContextUtil.getUserInfo();

        UserInfoVo userInfoVo = new UserInfoVo();
        BeanUtils.copyProperties(userInfo, userInfoVo);

        return userInfoVo;
    }


}
