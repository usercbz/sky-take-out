package com.sky.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.sky.entity.OrderDetail;

import java.util.List;

public interface OrderDetailService extends IService<OrderDetail> {
    /**
     * 查询订单详情
     *
     * @param orderId 订单id
     * @return 订单详情
     */
    List<OrderDetail> queryDetailByOrderId(Long orderId);
}
