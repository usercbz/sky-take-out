package com.sky.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.sky.entity.Dish;
import com.sky.vo.DishVO;

import java.util.List;


public interface DishMapper extends BaseMapper<Dish> {

    DishVO getDishById(Long id);

    List<DishVO> getDishesByCategoryId(Long categoryId);
}
