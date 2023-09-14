package com.sky.service.impl;

import com.sky.constant.StatusConstant;
import com.sky.entity.Dish;
import com.sky.entity.Orders;
import com.sky.entity.Setmeal;
import com.sky.entity.User;
import com.sky.service.*;
import com.sky.vo.BusinessDataVO;
import com.sky.vo.DishOverViewVO;
import com.sky.vo.OrderOverViewVO;
import com.sky.vo.SetmealOverViewVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

@Service
public class WorkspaceServiceImpl implements WorkspaceService {

    @Autowired
    private OrderService orderService;
    @Autowired
    private UserService userService;
    @Autowired
    private DishService dishService;
    @Autowired
    private SetmealService setmealService;

    /**
     * 查询今日运营数据
     *
     * @return
     */
    @Override
    public BusinessDataVO getBusinessData() {
        //日期
        LocalDate date = LocalDate.now();
        LocalDateTime begin = LocalDateTime.of(date, LocalTime.MIN);
        LocalDateTime end = LocalDateTime.of(date, LocalTime.MAX);

        Double turnover;//营业额
        int validOrderCount;//有效订单数
        double orderCompletionRate;//订单完成率
        double unitPrice;//平均客单价

        //获取当天营业额
        turnover = orderService.getAmountCount(begin, end);

        if (turnover == null || turnover == 0D) {
            turnover = 0D;
            validOrderCount = 0;
            orderCompletionRate = 0D;
            unitPrice = 0D;
        } else {
            //获取当天订单数
            List<Orders> orders = orderService.getOrdersByDate(begin, end);
            int orderCount = orders.size();
            //有效订单数
            validOrderCount = (int) orders.stream()
                    .filter(order -> order.getStatus() == Orders.COMPLETED)
                    .count();
            //完成率
            orderCompletionRate = (double) validOrderCount / (double) orderCount;
            //客单价
            unitPrice = turnover / validOrderCount;
        }
        //获取今日新增用户
        List<User> users = userService.getUsersByTime(begin, end);

        return BusinessDataVO.builder()
                .turnover(turnover)
                .validOrderCount(validOrderCount)
                .orderCompletionRate(orderCompletionRate)
                .unitPrice(unitPrice)
                .newUsers(users.size())
                .build();
    }

    /**
     * 查询订单管理数据
     *
     * @return
     */
    @Override
    public OrderOverViewVO getOverviewOrders() {

        int waitingOrders = 0;
        int deliveredOrders = 0;
        int completedOrders = 0;
        int cancelledOrders = 0;
        int allOrders;

        //获取订单数据
        List<Orders> orders = orderService.list();
        allOrders = orders.size();
        for (Orders order : orders) {
            Integer status = order.getStatus();
            if (status == Orders.TO_BE_CONFIRMED)
                waitingOrders++;
            if (status == Orders.CONFIRMED)
                deliveredOrders++;
            if (status == Orders.COMPLETED)
                completedOrders++;
            if (status == Orders.CANCELLED)
                cancelledOrders++;
        }

        return OrderOverViewVO.builder()
                .waitingOrders(waitingOrders)
                .deliveredOrders(deliveredOrders)
                .completedOrders(completedOrders)
                .cancelledOrders(cancelledOrders)
                .allOrders(allOrders)
                .build();
    }

    @Override
    public SetmealOverViewVO getOverviewSetmeal() {
        List<Setmeal> list = setmealService.list();
        //总数量
        int number = list.size();
        //统计起售商品
        int sold = (int) list.stream()
                .filter(setmeal -> setmeal.getStatus() == StatusConstant.ENABLE)
                .count();
        return new SetmealOverViewVO(sold, number - sold);
    }

    @Override
    public DishOverViewVO getOverviewDishes() {
        List<Dish> list = dishService.list();
        //总数量
        int number = list.size();
        //统计起售菜品
        int sold = (int) list.stream()
                .filter(dish -> dish.getStatus() == StatusConstant.ENABLE)
                .count();
        return new DishOverViewVO(sold, number - sold);
    }
}
