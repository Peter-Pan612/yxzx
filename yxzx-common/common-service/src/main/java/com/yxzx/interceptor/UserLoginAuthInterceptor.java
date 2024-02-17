package com.yxzx.interceptor;

import com.alibaba.fastjson2.JSON;
import com.yxzx.model.entity.user.UserInfo;
import com.yxzx.utils.AuthContextUtil;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.servlet.HandlerInterceptor;

/**
 * @author: Peter
 * @description:
 * @date: 2024/2/17 16:17
 */
public class UserLoginAuthInterceptor implements HandlerInterceptor {

    @Autowired
    private RedisTemplate<String, String> redisTemplate;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String token = request.getHeader("token");
        String userJson = redisTemplate.opsForValue().get("user:yxzx:" + token);

        //放到threadlocal
        AuthContextUtil.setUserInfo(JSON.parseObject(userJson, UserInfo.class));

        return true;
    }
}
