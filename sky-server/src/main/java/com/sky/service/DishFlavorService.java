package com.sky.service;


import com.baomidou.mybatisplus.extension.service.IService;
import com.sky.entity.DishFlavor;

import java.util.List;

public interface DishFlavorService extends IService<DishFlavor> {
    /**
     * 删除口味
     *
     * @param dishId 菜品id
     */
    void deleteFlavorsByDishId(Long dishId);

    /**
     * 添加菜品口味信息
     *
     * @param dishId  菜品id
     * @param flavors 口味信息
     */
    void addDishFlavors(Long dishId, List<DishFlavor> flavors);

    /**
     * 获取菜品口味
     *
     * @param dishId 菜品id
     * @return 口味集合
     */
    List<DishFlavor> getDishFlavorByDishId(Long dishId);
}
