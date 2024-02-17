package com.yxzx.service.impl;

import com.yxzx.mapper.UserAddressMapper;
import com.yxzx.model.entity.user.UserAddress;
import com.yxzx.service.UserAddressService;
import com.yxzx.utils.AuthContextUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author: Peter
 * @description:
 * @date: 2024/2/17 17:40
 */
@Service
public class UserAddressServiceImpl implements UserAddressService {

    @Autowired
    private UserAddressMapper userAddressMapper;

    @Override
    public List<UserAddress> findUserAddressList() {
        Long userId = AuthContextUtil.getUserInfo().getId();

        return userAddressMapper.findUserAddressList(userId);
    }

    @Override
    public UserAddress getUserAddress(Long id) {
        return userAddressMapper.getUserAddress(id);
    }
}
