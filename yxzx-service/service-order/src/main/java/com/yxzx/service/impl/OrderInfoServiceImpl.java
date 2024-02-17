package com.yxzx.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.yxzx.exception.yxzxException;
import com.yxzx.feign.cart.CartFeignClient;
import com.yxzx.feign.product.ProductFeignClient;
import com.yxzx.feign.user.UserFeignClient;
import com.yxzx.mapper.OrderInfoMapper;
import com.yxzx.mapper.OrderItemMapper;
import com.yxzx.mapper.OrderLogMapper;
import com.yxzx.model.dto.h5.OrderInfoDto;
import com.yxzx.model.entity.h5.CartInfo;
import com.yxzx.model.entity.order.OrderInfo;
import com.yxzx.model.entity.order.OrderItem;
import com.yxzx.model.entity.order.OrderLog;
import com.yxzx.model.entity.product.ProductSku;
import com.yxzx.model.entity.user.UserAddress;
import com.yxzx.model.entity.user.UserInfo;
import com.yxzx.model.vo.common.ResultCodeEnum;
import com.yxzx.model.vo.h5.TradeVo;
import com.yxzx.service.OrderInfoService;
import com.yxzx.utils.AuthContextUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/**
 * @author: Peter
 * @description:
 * @date: 2024/2/17 17:46
 */
@Service
public class OrderInfoServiceImpl implements OrderInfoService {


    @Autowired
    private ProductFeignClient productFeignClient;

    @Autowired
    private UserFeignClient userFeignClient;

    @Autowired
    private OrderInfoMapper orderInfoMapper;

    @Autowired
    private OrderItemMapper orderItemMapper;

    @Autowired
    private OrderLogMapper orderLogMapper;

    @Autowired
    private CartFeignClient cartFeignClient;

    //结算
    @Override
    public TradeVo getTrade() {
        List<CartInfo> cartInfoList = cartFeignClient.getAllCkecked();
        //创建list结合用于封装订单项
        List<OrderItem> orderItemList = new ArrayList<>();
        for (CartInfo cartInfo : cartInfoList) {
            OrderItem orderItem = new OrderItem();
            orderItem.setSkuId(cartInfo.getSkuId());
            orderItem.setSkuName(cartInfo.getSkuName());
            orderItem.setSkuNum(cartInfo.getSkuNum());
            orderItem.setSkuPrice(cartInfo.getCartPrice());
            orderItem.setThumbImg(cartInfo.getImgUrl());
            orderItemList.add(orderItem);

            orderItemList.add(orderItem);
        }
        //获取订单支付总金额
        BigDecimal totalAmount = new BigDecimal(0);
        for (OrderItem orderItem : orderItemList) {
            totalAmount = totalAmount.add(orderItem.getSkuPrice().multiply(new BigDecimal(orderItem.getSkuNum())));
        }

        TradeVo tradeVo = new TradeVo();
        tradeVo.setOrderItemList(orderItemList);
        tradeVo.setTotalAmount(totalAmount);


        return tradeVo;
    }

    //生成订单
    @Override
    public Long submitOrder(OrderInfoDto orderInfoDto) {
        //1 获取所有订单项目list
        List<OrderItem> orderItemList = orderInfoDto.getOrderItemList();

        //2 判断list为空，抛出异常
        if (CollectionUtils.isEmpty(orderItemList)) {
            throw new yxzxException(ResultCodeEnum.DATA_ERROR);
        }

        //3 校验商品库存是否充足
        //遍历list集合，得到每个orderItem
        for (OrderItem orderItem : orderItemList) {
            //根据skuid获取sku信息  //远程调用获取商品sku信息
            ProductSku productSku = productFeignClient.getBySkuId(orderItem.getSkuId());
            if (productSku == null) {
                throw new yxzxException(ResultCodeEnum.DATA_ERROR);
            }
            //校验每个item库存量
            if (orderItem.getSkuNum() > productSku.getSaleNum().intValue()) {
                throw new yxzxException(ResultCodeEnum.STOCK_LESS);
            }

        }

        //4 添加数据到order_info表
        //封装数据

        OrderInfo orderInfo = new OrderInfo();
        UserInfo userInfo = AuthContextUtil.getUserInfo();
        //订单编号
        orderInfo.setOrderNo(String.valueOf(System.currentTimeMillis()));
        //用户id
        orderInfo.setUserId(userInfo.getId());
        //用户昵称
        orderInfo.setNickName(userInfo.getNickName());

        //封装收货地址信息
        Long userAddressId = orderInfoDto.getUserAddressId();
        //远程调用：远程调用收货地址
        UserAddress userAddress = userFeignClient.getUserAddress(userAddressId);

        orderInfo.setReceiverName(userAddress.getName());
        orderInfo.setReceiverPhone(userAddress.getPhone());
        orderInfo.setReceiverTagName(userAddress.getTagName());
        orderInfo.setReceiverProvince(userAddress.getProvinceCode());
        orderInfo.setReceiverCity(userAddress.getCityCode());
        orderInfo.setReceiverDistrict(userAddress.getDistrictCode());
        orderInfo.setReceiverAddress(userAddress.getFullAddress());
        //订单金额
        BigDecimal totalAmount = new BigDecimal(0);
        for (OrderItem orderItem : orderItemList) {
            totalAmount = totalAmount.add(orderItem.getSkuPrice().multiply(new BigDecimal(orderItem.getSkuNum())));
        }
        orderInfo.setTotalAmount(totalAmount);
        orderInfo.setCouponAmount(new BigDecimal(0));
        orderInfo.setOriginalTotalAmount(totalAmount);
        orderInfo.setFeightFee(orderInfoDto.getFeightFee());
        orderInfo.setPayType(2);
        orderInfo.setOrderStatus(0);

        orderInfoMapper.save(orderInfo);

        //5 添加数据到order_item表
        //添加list里面数据，把每个orderItem添加表
        for (OrderItem orderItem : orderItemList) {
            //设置对应订单id
            orderItem.setOrderId(orderInfo.getId());
            orderItemMapper.save(orderItem);
        }

        //6 添加数据到order_log表
        OrderLog orderLog = new OrderLog();
        orderLog.setOrderId(orderInfo.getId());
        orderLog.setProcessStatus(0);
        orderLog.setNote("提交订单");
        orderLogMapper.save(orderLog);

        //7 把生成订单商品，从购物车删除
        cartFeignClient.deleteChecked();

        //8 返回订单id
        return orderInfo.getId();
    }

    @Override
    public OrderInfo getOrderInfo(Long orderId) {
        return orderInfoMapper.getById(orderId);
    }

    @Override
    public TradeVo buy(Long skuId) {
        // 查询商品
        ProductSku productSku = productFeignClient.getBySkuId(skuId);
        List<OrderItem> orderItemList = new ArrayList<>();
        OrderItem orderItem = new OrderItem();
        orderItem.setSkuId(skuId);
        orderItem.setSkuName(productSku.getSkuName());
        orderItem.setSkuNum(1);
        orderItem.setSkuPrice(productSku.getSalePrice());
        orderItem.setThumbImg(productSku.getThumbImg());
        orderItemList.add(orderItem);

        // 计算总金额
        BigDecimal totalAmount = productSku.getSalePrice();
        TradeVo tradeVo = new TradeVo();
        tradeVo.setTotalAmount(totalAmount);
        tradeVo.setOrderItemList(orderItemList);

        // 返回
        return tradeVo;
    }

    @Override
    public PageInfo<OrderInfo> findUserPage(Integer page, Integer limit, Integer orderStatus) {
        PageHelper.startPage(page, limit);
        Long userId = AuthContextUtil.getUserInfo().getId();
        List<OrderInfo> orderInfoList = orderInfoMapper.findUserPage(userId, orderStatus);

        orderInfoList.forEach(orderInfo -> {
            List<OrderItem> orderItem = orderItemMapper.findByOrderId(orderInfo.getId());
            orderInfo.setOrderItemList(orderItem);
        });

        return new PageInfo<>(orderInfoList);

    }
}
