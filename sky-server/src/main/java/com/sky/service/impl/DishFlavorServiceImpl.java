package com.sky.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.sky.entity.DishFlavor;
import com.sky.mapper.DishFlavorMapper;
import com.sky.service.DishFlavorService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DishFlavorServiceImpl extends ServiceImpl<DishFlavorMapper, DishFlavor> implements DishFlavorService {
    @Override
    public void deleteFlavorsByDishId(Long dishId) {
        LambdaQueryWrapper<DishFlavor> dishFlavorLambdaQueryWrapper = new LambdaQueryWrapper<>();
        dishFlavorLambdaQueryWrapper.eq(DishFlavor::getDishId, dishId);
        remove(dishFlavorLambdaQueryWrapper);
    }

    @Override
    public void addDishFlavors(Long dishId, List<DishFlavor> flavors) {
        for (DishFlavor flavor :
                flavors) {
            //保存口味
            flavor.setId(null);
            flavor.setDishId(dishId);
            save(flavor);
        }
    }

    @Override
    public List<DishFlavor> getDishFlavorByDishId(Long dishId) {
        return list(new LambdaQueryWrapper<DishFlavor>().eq(DishFlavor::getDishId, dishId));
    }
}
