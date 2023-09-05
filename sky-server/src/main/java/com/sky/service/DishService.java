package com.sky.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.sky.entity.Dish;

import java.util.List;

public interface DishService extends IService<Dish> {

    List<Dish> getDishByCategoryId(Long categoryId);
}
