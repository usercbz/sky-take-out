package com.sky.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.sky.entity.SetmealDish;

import java.util.List;

public interface SetmealDishService extends IService<SetmealDish> {

    /**
     * 查询套餐中的菜品
     *
     * @param setmealId 套餐id
     * @return 菜品集合
     */
    List<SetmealDish> queryDishBySetmealId(Long setmealId);

    /**
     * 保存套装与菜品关系
     *
     * @param setmealId     套餐id
     * @param setmealDishes 菜品集合
     */
    void saveDishesBySetmealId(Long setmealId, List<SetmealDish> setmealDishes);

    /**
     * 删除套餐中菜品
     *
     * @param setmealId 套餐id
     */
    void deleteDishBySetmealId(Long setmealId);

    /**
     * 查询菜品所在的套餐
     *
     * @param dishId 菜品id
     * @return 套餐菜品单项
     */
    List<SetmealDish> querySetmealByDishId(Long dishId);
}
