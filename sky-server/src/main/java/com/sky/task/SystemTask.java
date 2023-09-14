package com.sky.task;

import com.sky.entity.Orders;
import com.sky.service.OrderService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;

@Component
@Slf4j
public class SystemTask {

    @Autowired
    private OrderService orderService;

    /**
     * 处理订单超时
     */
    @Scheduled(cron = "0 * * * * ?") // 每分钟触发一次
    public void processTimeoutOrders() {
        LocalDateTime orderTime = LocalDateTime.now().plusMinutes(-15);
        //查询超时订单
        List<Orders> list = orderService.queryOrdersByStatusAndBeforeTime(Orders.PENDING_PAYMENT, orderTime);
        if (list == null || list.size() == 0)
            return;
        //取消订单、修改订单状态
        list.forEach(orders -> {
            //取消
            orders.setStatus(Orders.CANCELLED);
            //取消时间
            orders.setCancelTime(LocalDateTime.now());
            //取消原因
            orders.setCancelReason("订单超时");
        });
        //修改
        orderService.updateBatchById(list);
    }

    /**
     * 处理已完成的订单
     */
    @Scheduled(cron = "0 0 1 * * ?")//每天凌晨一点触发
    public void processCompletedOrders() {
        LocalDateTime orderTime = LocalDateTime.now().plusHours(-1);
        //查询订单
        List<Orders> list = orderService.queryOrdersByStatusAndBeforeTime(Orders.DELIVERY_IN_PROGRESS, orderTime);
        if (list == null || list.size() == 0)
            return;
        list.forEach(orders -> orders.setStatus(Orders.COMPLETED));
        orderService.updateBatchById(list);
    }
}
