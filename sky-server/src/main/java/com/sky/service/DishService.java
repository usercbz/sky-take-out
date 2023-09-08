package com.sky.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.sky.dto.DishDTO;
import com.sky.dto.DishPageQueryDTO;
import com.sky.entity.Dish;
import com.sky.result.PageResult;
import com.sky.result.Result;
import com.sky.vo.DishVO;

import java.util.List;

public interface DishService extends IService<Dish> {

    List<Dish> getDishByCategoryId(Long categoryId);

    Result<PageResult> queryDishPage(DishPageQueryDTO dishPageQueryDTO);

    Result<DishVO> queryDishById(Long id);

    Result<List<Dish>> queryDishListByCategoryId(Long categoryId);

    Result<String> addDish(DishDTO dishDTO);

    Result<String> updateStatusById(Long id, Integer status);

    Result<String> updateDish(DishDTO dishDTO);

    Result<String> removeDishById(String ids);
}
