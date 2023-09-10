package com.sky.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.sky.context.BaseContext;
import com.sky.dto.ShoppingCartDTO;
import com.sky.entity.Dish;
import com.sky.entity.Setmeal;
import com.sky.entity.ShoppingCart;
import com.sky.mapper.ShoppingCartMapper;
import com.sky.service.DishService;
import com.sky.service.SetmealService;
import com.sky.service.ShoppingCartService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ShoppingCartServiceImpl extends ServiceImpl<ShoppingCartMapper, ShoppingCart> implements ShoppingCartService {

    @Autowired
    private DishService dishService;
    @Autowired
    private SetmealService setmealService;

    @Override
    public void addShoppingCart(ShoppingCartDTO shoppingCartDTO) {
        //查询购物车中是否含有该菜品或套餐
        ShoppingCart shoppingCart = new ShoppingCart();
        BeanUtils.copyProperties(shoppingCartDTO, shoppingCart);
        shoppingCart.setUserId(BaseContext.getCurrentId());

        ShoppingCart queryShoppingCart = baseMapper.queryShoppingCart(shoppingCart);
        //判断
        if (queryShoppingCart != null) {
            //有数据 、在原有购物车清单 数量加一
            queryShoppingCart.setNumber(queryShoppingCart.getNumber() + 1);
            updateById(queryShoppingCart);
            //返回
            return;
        }
        //未查询到数据、插入新的购物车清单
        //判断是套餐还是菜品
        Long dishId = shoppingCart.getDishId();

        if (dishId != null) {
            //添加购物车清单是菜品
            Dish dish = dishService.getById(dishId);
            shoppingCart.setName(dish.getName());
            shoppingCart.setAmount(dish.getPrice());
            shoppingCart.setImage(dish.getImage());
        } else {
            //是套餐
            Long setmealId = shoppingCart.getSetmealId();
            Setmeal setmeal = setmealService.getById(setmealId);
            shoppingCart.setName(setmeal.getName());
            shoppingCart.setImage(setmeal.getImage());
            shoppingCart.setAmount(setmeal.getPrice());
        }
        //插入购物车清单
        save(shoppingCart);
    }

    @Override
    public List<ShoppingCart> queryShoppingCartList() {
        return list(new LambdaQueryWrapper<>(ShoppingCart.class)
                .eq(ShoppingCart::getUserId, BaseContext.getCurrentId()));
    }

    @Override
    public void subShoppingCart(ShoppingCartDTO shoppingCartDTO) {
        //判断购物车是否含有该菜品或套餐
        ShoppingCart shoppingCart = new ShoppingCart();
        BeanUtils.copyProperties(shoppingCartDTO, shoppingCart);
        shoppingCart.setUserId(BaseContext.getCurrentId());

        shoppingCart = baseMapper.queryShoppingCart(shoppingCart);
        if (shoppingCart != null) {
            //有数据 、在原有购物车清单 数量加一
            Integer number = shoppingCart.getNumber();
            if (number != 1) {
                shoppingCart.setNumber(number - 1);
                updateById(shoppingCart);
                //返回
                return;
            }
        }
        //删除
        //判断该操作数据为菜品还是套餐
        Long dishId = shoppingCartDTO.getDishId();
        if (dishId != null) {
            dishService.removeById(dishId);
        } else {
            setmealService.removeById(shoppingCartDTO.getSetmealId());
        }
    }

    @Override
    public void cleanShoppingCart() {
        remove(new LambdaQueryWrapper<ShoppingCart>()
                .eq(ShoppingCart::getUserId, BaseContext.getCurrentId()));
    }
}
