package com.sky.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.sky.dto.DishDTO;
import com.sky.dto.DishPageQueryDTO;
import com.sky.entity.Dish;
import com.sky.result.PageResult;
import com.sky.vo.DishVO;

import java.util.List;

public interface DishService extends IService<Dish> {

    List<Dish> getDishByCategoryId(Long categoryId);

    PageResult queryDishPage(DishPageQueryDTO dishPageQueryDTO);

    DishVO queryDishById(Long id);

    List<Dish> queryDishListByCategoryId(Long categoryId);

    void addDish(DishDTO dishDTO);

    void updateStatusById(Long id, Integer status);

    void updateDish(DishDTO dishDTO);

    void removeDishById(String ids);

    List<DishVO> getDishVOByCategoryId(Long categoryId);
}
