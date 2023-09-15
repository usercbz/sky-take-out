package com.sky.service.impl;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.sky.constant.MessageConstant;
import com.sky.context.BaseContext;
import com.sky.dto.*;
import com.sky.entity.AddressBook;
import com.sky.entity.OrderDetail;
import com.sky.entity.Orders;
import com.sky.entity.ShoppingCart;
import com.sky.exception.AddressBookBusinessException;
import com.sky.exception.OrderBusinessException;
import com.sky.exception.ShoppingCartBusinessException;
import com.sky.mapper.OrderMapper;
import com.sky.result.PageResult;
import com.sky.service.AddressBookService;
import com.sky.service.OrderDetailService;
import com.sky.service.OrderService;
import com.sky.service.ShoppingCartService;
import com.sky.vo.OrderPaymentVO;
import com.sky.vo.OrderStatisticsVO;
import com.sky.vo.OrderSubmitVO;
import com.sky.vo.OrderVO;
import com.sky.websocket.WebSocketServer;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.StringJoiner;

@Service
public class OrderServiceImpl extends ServiceImpl<OrderMapper, Orders> implements OrderService {

    private final OrderDetailService orderDetailService;
    private final ShoppingCartService shoppingCartService;
    private final AddressBookService addressBookService;
    private final WebSocketServer webSocketServer;

    public OrderServiceImpl(OrderDetailService orderDetailService, ShoppingCartService shoppingCartService, AddressBookService addressBookService, WebSocketServer webSocketServer) {
        this.orderDetailService = orderDetailService;
        this.shoppingCartService = shoppingCartService;
        this.addressBookService = addressBookService;
        this.webSocketServer = webSocketServer;
    }

    @Override
    @Transactional
    public OrderSubmitVO submit(OrdersSubmitDTO submitDTO) {
        //判断地址簿
        AddressBook addressBook = addressBookService.getById(submitDTO.getAddressBookId());
        if (addressBook == null) {
            throw new AddressBookBusinessException(MessageConstant.ADDRESS_BOOK_IS_NULL);
        }
        //查询购物车数据
        List<ShoppingCart> shoppingCartList = shoppingCartService.queryShoppingCartList();
        //判断购物车是否有数据
        if (shoppingCartList == null || shoppingCartList.size() == 0) {
            throw new ShoppingCartBusinessException(MessageConstant.SHOPPING_CART_IS_NULL);
        }
        //1、保存订单数据
        Orders orders = new Orders();
        BeanUtils.copyProperties(submitDTO, orders);
        //设置用户id
        orders.setUserId(BaseContext.getCurrentId());
        //设置订单编号
        orders.setOrderNumber(String.valueOf(System.currentTimeMillis()));
        //设置下单时间
        orders.setOrderTime(LocalDateTime.now());
        //设置电话
        orders.setPhone(addressBook.getPhone());
        //收货人
        orders.setConsignee(addressBook.getConsignee());
        //收货地址
        orders.setAddress(addressBook.getDetail());
        //保存
        save(orders);

        //2、保存订单详情
        Long ordersId = orders.getId();
        List<OrderDetail> orderDetails = new ArrayList<>();
        for (ShoppingCart shoppingCart : shoppingCartList) {
            //构建订单详情数据
            OrderDetail orderDetail = new OrderDetail();
            BeanUtils.copyProperties(shoppingCart, orderDetail);
            //id置空
            orderDetail.setId(null);
            //设置订单id
            orderDetail.setOrderId(ordersId);
            //添加到list
            orderDetails.add(orderDetail);
        }
        //保存
        orderDetailService.saveBatch(orderDetails);
        //3、清空购物车
        shoppingCartService.cleanShoppingCart();
        //4、封装返回VO数据
        OrderSubmitVO orderSubmitVO = new OrderSubmitVO();
        orderSubmitVO.setId(ordersId);
        orderSubmitVO.setOrderNumber(orders.getOrderNumber());
        orderSubmitVO.setOrderTime(orders.getOrderTime());
        orderSubmitVO.setOrderAmount(orders.getAmount());
        return orderSubmitVO;
    }

    @Override
    public OrderVO queryOrderDetail(Long orderId) {
        return getOrderVO(getById(orderId));
    }

    @Override
    public void cancelOrder(OrdersCancelDTO ordersCancelDTO) {
        //省略
        //查询订单
        Long orderId = ordersCancelDTO.getId();
        Orders orders = getById(orderId);
        if (orders == null) {
            throw new OrderBusinessException(MessageConstant.ORDER_NOT_FOUND);
        }
        //修改订单状态
        String cancelReason = ordersCancelDTO.getCancelReason();
        orders.setStatus(Orders.CANCELLED);
        //取消原因
        if (cancelReason == null) {
            orders.setCancelReason("用户取消订单");
        } else {
            orders.setCancelReason(cancelReason);
        }
        //取消时间
        orders.setCancelTime(LocalDateTime.now());

        updateById(orders);
    }

    @Override
    public void repetition(Long orderId) {
        Orders orders = getById(orderId);
        if (orders == null) {
            throw new OrderBusinessException(MessageConstant.ORDER_NOT_FOUND);
        }
        //将订单详情数据重新加入购物车
        List<OrderDetail> orderDetails = orderDetailService.queryDetailByOrderId(orderId);
        ArrayList<ShoppingCart> shoppingCarts = new ArrayList<>();
        //用户id
        Long userId = BaseContext.getCurrentId();
        orderDetails.forEach(orderDetail -> {
            ShoppingCart shoppingCart = new ShoppingCart();
            BeanUtils.copyProperties(orderDetail, shoppingCart);
            shoppingCart.setUserId(userId);
            shoppingCart.setId(null);
            shoppingCarts.add(shoppingCart);
        });
        //保存
        shoppingCartService.saveBatch(shoppingCarts);
    }

    @Override
    public void reminder(Long orderId) {

        Orders orders = getById(orderId);
        if (orders == null) {
            throw new OrderBusinessException(MessageConstant.ORDER_NOT_FOUND);
        }
        HashMap<String, Object> map = new HashMap<>();
        map.put("type", 2);
        map.put("orderId", orderId);
        map.put("content", "订单号:" + orders.getOrderNumber());

        String message = JSON.toJSONString(map);
        webSocketServer.sendToAllClient(message);
    }

    @Override
    public OrderPaymentVO payment(OrdersPaymentDTO ordersPaymentDTO) {
        //跳过微信支付
        //修改订单的状态
        String orderNumber = ordersPaymentDTO.getOrderNumber();
        //查询订单
        Orders orders = getOne(new LambdaQueryWrapper<>(Orders.class)
                .eq(Orders::getOrderNumber, orderNumber));
        //判断
        if (orders == null) {
            throw new OrderBusinessException(MessageConstant.ORDER_NOT_FOUND);
        }
        //设置订单状态等属性
        //结账时间
        orders.setCheckoutTime(LocalDateTime.now());
        //设置订单状态
        orders.setStatus(Orders.TO_BE_CONFIRMED);
        orders.setPayStatus(Orders.PAID);
        //修改
        updateById(orders);

        //支付成功、调用websocket
        HashMap<String, Object> map = new HashMap<>();
        map.put("type", 1);
        map.put("orderId", orders.getId());
        map.put("content", "订单号:" + orderNumber);

        String message = JSON.toJSONString(map);
        webSocketServer.sendToAllClient(message);

        return null;
    }

    @Override
    public PageResult conditionSearch(OrdersPageQueryDTO ordersPageQueryDTO) {

        int page = ordersPageQueryDTO.getPage();
        int pageSize = ordersPageQueryDTO.getPageSize();
        String number = ordersPageQueryDTO.getNumber();
        String phone = ordersPageQueryDTO.getPhone();
        Integer status = ordersPageQueryDTO.getStatus();
        LocalDateTime beginTime = ordersPageQueryDTO.getBeginTime();
        LocalDateTime endTime = ordersPageQueryDTO.getEndTime();

        Long userId = ordersPageQueryDTO.getUserId();

        LambdaQueryWrapper<Orders> queryWrapper = new LambdaQueryWrapper<>(Orders.class)
                .orderByDesc(Orders::getOrderTime);

        if (userId != null) queryWrapper.eq(Orders::getUserId, userId);
        if (status != null) queryWrapper.eq(Orders::getStatus, status);
        if (number != null) queryWrapper.like(Orders::getOrderNumber, number);
        if (phone != null) queryWrapper.eq(Orders::getPhone, phone);
        if (beginTime != null) queryWrapper.gt(Orders::getOrderTime, beginTime);
        if (endTime != null) queryWrapper.lt(Orders::getOrderTime, endTime);

        Page<Orders> ordersPage = page(new Page<>(page, pageSize), queryWrapper);
        //构建Vo
        List<Orders> ordersList = ordersPage.getRecords();
        ArrayList<OrderVO> orderVOS = new ArrayList<>();
        ordersList.stream().map(this::getOrderVO).forEach(orderVOS::add);
        return new PageResult(ordersPage.getTotal(), orderVOS);
    }

    @Override
    public OrderStatisticsVO statistics() {
        //获取订单
        List<Orders> orders = list();
        //构造结果集
        int toBeConfirmed = 0;
        int confirmed = 0;
        int deliveryInProgress = 0;
        for (Orders order : orders) {
            Integer status = order.getStatus();
            if (status == Orders.TO_BE_CONFIRMED) toBeConfirmed++;
            if (status == Orders.CONFIRMED) confirmed++;
            if (status == Orders.DELIVERY_IN_PROGRESS) deliveryInProgress++;
        }
        return new OrderStatisticsVO(toBeConfirmed, confirmed, deliveryInProgress);
    }

    @Override
    public void rejection(OrdersRejectionDTO rejectionDTO) {

        Long orderId = rejectionDTO.getId();
        String rejectionReason = rejectionDTO.getRejectionReason();
        Orders orders = getById(orderId);
        //订单状态
        orders.setCancelTime(LocalDateTime.now());
        orders.setCancelReason(rejectionReason);
        orders.setStatus(Orders.CANCELLED);
        orders.setRejectionReason(rejectionReason);
        orders.setPayStatus(Orders.REFUND);

        updateById(orders);
    }

    @Override
    public void confirm(Long orderId) {
        Orders orders = getById(orderId);
        //修改订单状态
        orders.setStatus(Orders.CONFIRMED);

        updateById(orders);

    }

    @Override
    public void delivery(Long orderId) {
        Orders orders = getById(orderId);
        //修改订单状态
        orders.setStatus(Orders.DELIVERY_IN_PROGRESS);
        orders.setDeliveryTime(LocalDateTime.now());
        updateById(orders);
    }

    @Override
    public void complete(Long orderId) {
        Orders orders = getById(orderId);
        //修改订单状态
        orders.setStatus(Orders.COMPLETED);

        updateById(orders);
    }

    @Override
    public List<Orders> queryOrdersByStatusAndBeforeTime(Integer status, LocalDateTime orderTime) {
        return list(new LambdaQueryWrapper<>(Orders.class)
                .eq(Orders::getStatus, status)
                .lt(Orders::getOrderTime, orderTime));
    }

    @Override
    public List<Orders> getOrdersByDate(LocalDateTime beginTime, LocalDateTime endTime) {
        return list(new LambdaUpdateWrapper<>(Orders.class)
                .between(Orders::getOrderTime, beginTime, endTime));
    }

    @Override
    public List<GoodsSalesDTO> salesTop10Report(LocalDateTime begin, LocalDateTime end) {
        /*
        获取销售排名
        select od.name,sum(number) as number
        from order_detail od,orders o
        where od.order_id = orders.id
        and orders.status = 5
        and order_time  between ? and  ?
        group by od.name
        order by number desc
        limit 0,10
         */
        return baseMapper.salesTop10Report(begin, end);
    }

    @Override
    public Double getAmountCount(LocalDateTime dateBegin, LocalDateTime dateEnd) {
        // select sum(amount) from orders where status = 5 and order_time between ? and ?
        return baseMapper.getAmountCount(dateBegin,dateEnd);
    }

    private OrderVO getOrderVO(Orders orders) {

        List<OrderDetail> orderDetails = orderDetailService.queryDetailByOrderId(orders.getId());
        OrderVO orderVO = new OrderVO();
        BeanUtils.copyProperties(orders, orderVO);
        orderVO.setOrderDetailList(orderDetails);

        StringJoiner orderDishes = new StringJoiner(" ");
        orderDetails.stream()
                .map(orderDetail -> orderDetail.getName() + "*" + orderDetail.getNumber() + ";")
                .forEach(orderDishes::add);
        //菜品信息
        orderVO.setOrderDishes(orderDishes.toString());

        return orderVO;
    }

}
