package com.sky.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.sky.dto.*;
import com.sky.entity.Orders;
import com.sky.result.PageResult;
import com.sky.vo.OrderPaymentVO;
import com.sky.vo.OrderStatisticsVO;
import com.sky.vo.OrderSubmitVO;
import com.sky.vo.OrderVO;

import java.time.LocalDateTime;
import java.util.List;


public interface OrderService extends IService<Orders> {

    /**
     * 用户下单
     *
     * @param submitDTO
     * @return
     */
    OrderSubmitVO submit(OrdersSubmitDTO submitDTO);

    /**
     * 查询订单详情数据
     *
     * @param orderId 订单id
     * @return 订单详情
     */
    OrderVO queryOrderDetail(Long orderId);

    /**
     * 取消订单
     *
     * @param ordersCancelDTO 订单取消原因
     */
    void cancelOrder(OrdersCancelDTO ordersCancelDTO);

    /**
     * 再来一单
     *
     * @param orderId 订单id
     */
    void repetition(Long orderId);

    /**
     * 用户催单
     *
     * @param orderId
     */
    void reminder(Long orderId);

    /**
     * 用户支付
     *
     * @param ordersPaymentDTO
     * @return
     */
    OrderPaymentVO payment(OrdersPaymentDTO ordersPaymentDTO);

    /**
     * 条件查询订单
     *
     * @param ordersPageQueryDTO 查询条件
     * @return 订单分页数据
     */
    PageResult conditionSearch(OrdersPageQueryDTO ordersPageQueryDTO);

    /**
     * 统计订单状态数据
     *
     * @return 订单状态数据
     */
    OrderStatisticsVO statistics();

    /**
     * 拒单
     *
     * @param rejectionDTO 拒单原因
     */
    void rejection(OrdersRejectionDTO rejectionDTO);

    /**
     * 商家接单
     *
     * @param orderId
     */
    void confirm(Long orderId);

    /**
     * 派送订单
     *
     * @param orderId
     */
    void delivery(Long orderId);

    /**
     * 订单完成
     *
     * @param orderId
     */
    void complete(Long orderId);

    List<Orders> queryOrdersByStatusAndBeforeTime(Integer status, LocalDateTime orderTime);


    /**
     * 获取某个时间段的订单集合
     *
     * @param beginTime 起始时间
     * @param endTime   结束时间
     * @return 订单集合
     */
    List<Orders> getOrdersByDate(LocalDateTime beginTime, LocalDateTime endTime);

    /**
     * 获取销量前十的商品
     * @param begin
     * @param end
     * @return
     */
    List<GoodsSalesDTO> salesTop10Report(LocalDateTime begin, LocalDateTime end);

    /**
     * 统计金额
     * @param dateBegin
     * @param dateEnd
     * @return
     */
    Double getAmountCount(LocalDateTime dateBegin, LocalDateTime dateEnd);
}
