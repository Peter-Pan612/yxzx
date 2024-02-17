package com.yxzx.mapper;

import com.yxzx.model.entity.user.UserInfo;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface UserInfoMapper {
    UserInfo selectByUserName(String username);

    void save(UserInfo userInfo);
}
