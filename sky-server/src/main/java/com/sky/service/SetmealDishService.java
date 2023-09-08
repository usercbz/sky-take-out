package com.sky.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.sky.entity.SetmealDish;

import java.util.List;

public interface SetmealDishService extends IService<SetmealDish> {
    List<SetmealDish> queryDishBySetmealId(Long setmealId);

    void saveDishesBySetmealId(Long setmealId, List<SetmealDish> setmealDishes);

    void deleteDishBySetmealId(Long setmealId);
}
