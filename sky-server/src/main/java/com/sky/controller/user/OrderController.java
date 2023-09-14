package com.sky.controller.user;

import com.sky.context.BaseContext;
import com.sky.dto.OrdersCancelDTO;
import com.sky.dto.OrdersPageQueryDTO;
import com.sky.dto.OrdersPaymentDTO;
import com.sky.dto.OrdersSubmitDTO;
import com.sky.result.PageResult;
import com.sky.result.Result;
import com.sky.service.OrderService;
import com.sky.vo.OrderPaymentVO;
import com.sky.vo.OrderSubmitVO;
import com.sky.vo.OrderVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController("userOrderController")
@RequestMapping("/user/order")
public class OrderController {
    @Autowired
    private OrderService orderService;

    @PostMapping("submit")
    public Result<OrderSubmitVO> submitOrder(@RequestBody OrdersSubmitDTO submitDTO) {
        OrderSubmitVO orderSubmitVO = orderService.submit(submitDTO);
        return Result.success(orderSubmitVO);
    }

    @GetMapping("historyOrders")
    public Result<PageResult> getHistoryOrders(OrdersPageQueryDTO ordersPageQueryDTO) {
        ordersPageQueryDTO.setUserId(BaseContext.getCurrentId());
        PageResult pageResult = orderService.conditionSearch(ordersPageQueryDTO);
        return Result.success(pageResult);
    }

    @GetMapping("/orderDetail/{id}")
    public Result<OrderVO> getOrderDetail(@PathVariable Long id) {
        OrderVO orderVO = orderService.queryOrderDetail(id);
        return Result.success(orderVO);
    }

    @PutMapping("/cancel/{id}")
    public Result<Object> cancelOrder(@PathVariable Long id) {
        orderService.cancelOrder(new OrdersCancelDTO(id, null));
        return Result.success();
    }

    @PostMapping("/repetition/{id}")
    public Result<Object> repetitionOrder(@PathVariable Long id) {
        orderService.repetition(id);
        return Result.success();
    }

    @PutMapping("payment")
    public Result<OrderPaymentVO> paymentOrder(@RequestBody OrdersPaymentDTO ordersPaymentDTO) {
        OrderPaymentVO orderPaymentVO = orderService.payment(ordersPaymentDTO);
        return Result.success(orderPaymentVO);
    }

    @GetMapping("/reminder/{id}")
    public Result<Object> reminder(@PathVariable Long id) {
        orderService.reminder(id);
        return Result.success();
    }
}
