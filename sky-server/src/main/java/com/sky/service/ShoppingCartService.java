package com.sky.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.sky.dto.ShoppingCartDTO;
import com.sky.entity.ShoppingCart;

import java.util.List;

public interface ShoppingCartService extends IService<ShoppingCart> {

    void addShoppingCart(ShoppingCartDTO shoppingCartDTO);

    List<ShoppingCart> queryShoppingCartList();

    void subShoppingCart(ShoppingCartDTO shoppingCartDTO);

    void cleanShoppingCart();

}
