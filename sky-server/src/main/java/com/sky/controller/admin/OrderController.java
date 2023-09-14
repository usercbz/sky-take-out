package com.sky.controller.admin;

import com.sky.dto.OrdersCancelDTO;
import com.sky.dto.OrdersPageQueryDTO;
import com.sky.dto.OrdersRejectionDTO;
import com.sky.entity.Orders;
import com.sky.result.PageResult;
import com.sky.result.Result;
import com.sky.service.OrderService;
import com.sky.vo.OrderStatisticsVO;
import com.sky.vo.OrderVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/admin/order")
public class OrderController {

    @Autowired
    private OrderService orderService;

    /**
     * 查看订单详情
     *
     * @param id
     * @return
     */
    @GetMapping("/details/{id}")
    public Result<OrderVO> getOrderDetails(@PathVariable Long id) {
        OrderVO orderVO = orderService.queryOrderDetail(id);
        return Result.success(orderVO);
    }

    /**
     * 条件查询订单
     *
     * @param ordersPageQueryDTO
     * @return
     */
    @GetMapping("conditionSearch")
    public Result<PageResult> SearchForOrders(OrdersPageQueryDTO ordersPageQueryDTO) {
        PageResult pageResult = orderService.conditionSearch(ordersPageQueryDTO);
        return Result.success(pageResult);
    }

    /**
     * 统计订单状态数量
     *
     * @return
     */
    @GetMapping("statistics")
    public Result<OrderStatisticsVO> statistics() {
        OrderStatisticsVO orderStatisticsVO = orderService.statistics();
        return Result.success(orderStatisticsVO);
    }

    /**
     * 订单完成
     *
     * @param id
     * @return
     */
    @PutMapping("/complete/{id}")
    public Result<Object> completeOrder(@PathVariable Long id) {

        orderService.complete(id);
        return Result.success();
    }

    /**
     * 订单取消
     *
     * @param ordersCancelDTO
     * @return
     */
    @PutMapping("cancel")
    public Result<Object> cancelOrder(@RequestBody OrdersCancelDTO ordersCancelDTO) {
        orderService.cancelOrder(ordersCancelDTO);
        return Result.success();
    }

    /**
     * 拒单
     *
     * @param rejectionDTO
     * @return
     */
    @PutMapping("rejection")
    public Result<Object> rejection(@RequestBody OrdersRejectionDTO rejectionDTO) {

        orderService.rejection(rejectionDTO);
        return Result.success();
    }

    /**
     * 接单
     *
     * @param orders
     * @return
     */
    @PutMapping("confirm")
    public Result<Object> confirm(@RequestBody Orders orders) {

        orderService.confirm(orders.getId());
        return Result.success();
    }

    /**
     * 派送
     *
     * @param id
     * @return
     */
    @PutMapping("/delivery/{id}")
    public Result<Object> delivery(@PathVariable Long id) {

        orderService.delivery(id);
        return Result.success();
    }
}
