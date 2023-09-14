package com.sky.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.sky.dto.ShoppingCartDTO;
import com.sky.entity.ShoppingCart;

import java.util.List;

public interface ShoppingCartService extends IService<ShoppingCart> {

    /**
     * 添加购物车清单
     *
     * @param shoppingCartDTO 清单数据
     */
    void addShoppingCart(ShoppingCartDTO shoppingCartDTO);

    /**
     * 查询购物车列表
     *
     * @return 购物车列表
     */
    List<ShoppingCart> queryShoppingCartList();

    /**
     * 减少购物清单项
     *
     * @param shoppingCartDTO 购物车数据
     */
    void subShoppingCart(ShoppingCartDTO shoppingCartDTO);

    /**
     * 清空购物车
     */
    void cleanShoppingCart();

}
