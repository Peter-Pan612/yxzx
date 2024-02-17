package com.yxzx.service;

import com.yxzx.model.dto.h5.UserLoginDto;
import com.yxzx.model.dto.h5.UserRegisterDto;
import com.yxzx.model.vo.h5.UserInfoVo;

public interface UserInfoService {
    void register(UserRegisterDto userRegisterDto);


    String login(UserLoginDto userLoginDto);

    UserInfoVo getCurrentUserInfo(String token);
}
