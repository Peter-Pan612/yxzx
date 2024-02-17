package com.yxzx.mapper;

import com.yxzx.model.entity.user.UserAddress;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface UserAddressMapper {
    List<UserAddress> findUserAddressList(Long userId);

    UserAddress getUserAddress(Long id);
}
