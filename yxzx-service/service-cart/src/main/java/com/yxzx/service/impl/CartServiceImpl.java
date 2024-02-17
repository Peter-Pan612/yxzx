package com.yxzx.service.impl;

import com.alibaba.fastjson2.JSON;
import com.yxzx.feign.product.ProductFeignClient;
import com.yxzx.model.entity.h5.CartInfo;
import com.yxzx.model.entity.product.ProductSku;
import com.yxzx.model.entity.user.UserInfo;
import com.yxzx.service.CartService;
import com.yxzx.utils.AuthContextUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author: Peter
 * @description:
 * @date: 2024/2/17 16:35
 */
@Service
public class CartServiceImpl implements CartService {

    @Autowired
    private RedisTemplate<String, String> redisTemplate;

    @Autowired
    private ProductFeignClient productFeignClient;

    @Override
    public void addToCart(Long skuId, Integer skuNum) {
        //1 必须登录状态，获取当前登录用户id，作为redis的hash类型的key值
        //从threadlocal获取用户信息即可
        Long userId = AuthContextUtil.getUserInfo().getId();
        //构建hash类型key名称
        String cartKey = this.getCartKey(userId);

        //2 因为购物车放到redis里面
        //从redis里面获取购物车数据，根据用户id + skuId获取（hash类型key+field）
        Object cartInfoObj = redisTemplate.opsForHash().get(cartKey, String.valueOf(skuId));

        //3 如果购物车存在添加商品，把商品数量相加
        CartInfo cartInfo = null;
        if (cartInfoObj != null) { //添加到购物车商品已经存在，数据++
            cartInfo = JSON.parseObject(cartInfoObj.toString(), CartInfo.class);
            //数量相加
            cartInfo.setSkuNum(cartInfo.getSkuNum() + skuNum);
            //设置数据:表示商品选中状态
            cartInfo.setIsChecked(1);
            cartInfo.setUpdateTime(new Date());
        } else {
            //4 如果购物车没有添加商品，直接商品添加购物车（添加到redis）
            //远程调用实现：nacos+openFeign 根据skuId获取商品信息
            cartInfo = new CartInfo();

            //远程调用实现:根据skuid获取商品sku信息
            ProductSku productSku = productFeignClient.getBySkuId(skuId);

            //设置相关数据到cartInfo里面
            cartInfo.setCartPrice(productSku.getSalePrice());
            cartInfo.setSkuNum(skuNum);
            cartInfo.setSkuId(skuId);
            cartInfo.setUserId(userId);
            cartInfo.setImgUrl(productSku.getThumbImg());
            cartInfo.setSkuName(productSku.getSkuName());
            cartInfo.setIsChecked(1);
            cartInfo.setCreateTime(new Date());
            cartInfo.setUpdateTime(new Date());

        }

        //添加到redis里面
        redisTemplate.opsForHash().put(cartKey, String.valueOf(skuId), JSON.toJSONString(cartInfo));


    }

    //查询购物车
    @Override
    public List<CartInfo> getCartList() {
        //1 构建查询的redis里面key值，根据当前userId
        Long userId = AuthContextUtil.getUserInfo().getId();
        String cartKey = this.getCartKey(userId);

        //2 根据key从redis里面hash类型获取所有value值 cartINfo
        List<Object> valueList = redisTemplate.opsForHash().values(cartKey);

        if (!CollectionUtils.isEmpty(valueList)) {
            List<CartInfo> cartInfoList = valueList.stream()
                    .map(cartInfoObj -> JSON.parseObject(cartInfoObj.toString(), CartInfo.class))
                    .sorted(((o1, o2) -> o2.getCreateTime().compareTo(o1.getCreateTime())))
                    .collect(Collectors.toList());

            return cartInfoList;
        }

        //空集合
        return new ArrayList<>();
    }

    @Override
    public void deleteCart(Long skuId) {
        Long userId = AuthContextUtil.getUserInfo().getId();
        String cartKey = this.getCartKey(userId);
        redisTemplate.opsForHash().delete(cartKey, String.valueOf(skuId));
    }

    @Override
    public void checkCart(Long skuId, Integer isChecked) {
        Long userId = AuthContextUtil.getUserInfo().getId();
        String cartKey = this.getCartKey(userId);

        //判断key是否包含filed
        Boolean hasKey = redisTemplate.opsForHash().hasKey(cartKey, String.valueOf(skuId));

        if (hasKey) {
            //根据key+field把value获取出来
            String cartInfoString = redisTemplate.opsForHash().get(cartKey, String.valueOf(skuId)).toString();

            //更新value里面选中状态
            CartInfo cartInfo = JSON.parseObject(cartInfoString, CartInfo.class);
            cartInfo.setIsChecked(isChecked);

            //放回到redis的hash类型里面
            redisTemplate.opsForHash().put(cartKey, String.valueOf(skuId),
                    JSON.toJSONString(cartInfo));

        }


    }

    @Override
    public void allCheckCart(Integer isChecked) {
        Long userId = AuthContextUtil.getUserInfo().getId();
        String cartKey = this.getCartKey(userId);

        // 根据key获取购物车所有value值
        List<Object> objectList = redisTemplate.opsForHash().values(cartKey);
        if (CollectionUtils.isEmpty(objectList)) {
            List<CartInfo> cartInfoList = objectList.stream().map(objcet -> JSON.parseObject(objectList.toString(), CartInfo.class))
                    .collect(Collectors.toList());

            //把每个商品isChecked进行更新
            cartInfoList.forEach(cartInfo -> {
                cartInfo.setIsChecked(isChecked);
                redisTemplate.opsForHash().put(cartKey, String.valueOf(cartInfo.getSkuId()),
                        JSON.toJSONString(cartInfo));
            });
        }


    }

    @Override
    public void clearCart() {
        Long userId = AuthContextUtil.getUserInfo().getId();
        String cartKey = getCartKey(userId);
        redisTemplate.delete(cartKey);
    }

    //远程调用
    @Override
    public List<CartInfo> getAllCkecked() {
        Long userId = AuthContextUtil.getUserInfo().getId();
        String cartKey = getCartKey(userId);

        List<Object> objectList = redisTemplate.opsForHash().values(cartKey);
        if (!CollectionUtils.isEmpty(objectList)) {
            List<CartInfo> cartInfoList = objectList.stream().map(object ->
                            JSON.parseObject(object.toString(), CartInfo.class))
                    .filter(cartInfo -> cartInfo.getIsChecked() == 1)
                    .collect(Collectors.toList());

            return cartInfoList;
        }

        return new ArrayList<>();
    }

    //远程调用：删除生成订单的购物车商品
    @Override
    public void deleteChecked() {
        Long userId = AuthContextUtil.getUserInfo().getId();
        String cartKey = getCartKey(userId);

        //根据key获取redis的value值
        List<Object> objectList = redisTemplate.opsForHash().values(cartKey);

        //删除选中商品
        objectList.stream().map(object -> JSON.parseObject(object.toString(),CartInfo.class))
                .filter(cartInfo -> cartInfo.getIsChecked()==1)
                .forEach(cartInfo -> redisTemplate.opsForHash().delete(cartKey,String.valueOf(cartInfo.getSkuId())));


    }

    private String getCartKey(Long userId) {
        //定义key user:cart:userId
        return "user:cart:" + userId;
    }
}
