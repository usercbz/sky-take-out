package com.sky.service;


import com.baomidou.mybatisplus.extension.service.IService;
import com.sky.entity.DishFlavor;

import java.util.List;

public interface DishFlavorService extends IService<DishFlavor> {
    void deleteFlavorsByDishId(Long dishId);

    void addDishFlavors(Long dishId, List<DishFlavor> flavors);

    List<DishFlavor> getDishFlavorByDishId(Long dishId);
}
