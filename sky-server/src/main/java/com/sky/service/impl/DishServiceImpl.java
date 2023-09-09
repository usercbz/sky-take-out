package com.sky.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.sky.constant.MessageConstant;
import com.sky.dto.DishDTO;
import com.sky.dto.DishPageQueryDTO;
import com.sky.entity.Dish;
import com.sky.entity.DishFlavor;
import com.sky.mapper.DishMapper;
import com.sky.result.PageResult;
import com.sky.result.Result;
import com.sky.service.DishFlavorService;
import com.sky.service.DishService;
import com.sky.vo.DishVO;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class DishServiceImpl extends ServiceImpl<DishMapper, Dish> implements DishService {

    @Autowired
    private DishFlavorService dishFlavorService;


    @Override
    public List<Dish> getDishByCategoryId(Long categoryId) {
        LambdaQueryWrapper<Dish> wrapper = new LambdaQueryWrapper<Dish>().eq(Dish::getCategoryId, categoryId);
        return list(wrapper);
    }

    @Override
    public Result<PageResult> queryDishPage(DishPageQueryDTO dishPageQueryDTO) {

        Integer categoryId = dishPageQueryDTO.getCategoryId();
        String name = dishPageQueryDTO.getName();
        Integer status = dishPageQueryDTO.getStatus();

        LambdaQueryWrapper<Dish> dishLambdaQueryWrapper = new LambdaQueryWrapper<>();

        if (name != null) {
            dishLambdaQueryWrapper.like(Dish::getName, name);
        }

        if (status != null) {
            dishLambdaQueryWrapper.eq(Dish::getStatus, status);
        }

        if (categoryId != null) {
            dishLambdaQueryWrapper.eq(Dish::getCategoryId, categoryId);
        }

        Page<Dish> page = page(new Page<>(dishPageQueryDTO.getPage(), dishPageQueryDTO.getPageSize()), dishLambdaQueryWrapper);


        return Result.success(new PageResult(page.getTotal(), page.getRecords()));
    }

    @Override
    public Result<DishVO> queryDishById(Long id) {
        DishVO dishVO = baseMapper.getDishById(id);
        return Result.success(dishVO);
    }

    @Override
    public Result<List<Dish>> queryDishListByCategoryId(Long categoryId) {
        return Result.success(list(Wrappers.lambdaQuery(Dish.class).eq(Dish::getCategoryId, categoryId)));
    }


    @Override
    @Transactional
    public Result<String> addDish(DishDTO dishDTO) {

        //创建菜品
        Dish dish = new Dish();
        BeanUtils.copyProperties(dishDTO, dish);

        //保存菜品
        if (!save(dish)) {
            //保存失败
            return Result.error(MessageConstant.ADD_FAIL);
        }
        //成功，取出保存后菜品id
        Long dishId = dish.getId();
        //创建菜品口味
        List<DishFlavor> flavors = dishDTO.getFlavors();

        //添加菜品口味
        dishFlavorService.addDishFlavors(dishId, flavors);

        return Result.success();
    }

    @Override
    public Result<String> updateStatusById(Long id, Integer status) {

        Dish dish = Dish.builder()
                .id(id)
                .status(status)
                .build();

        if (!updateById(dish)) {
            //失败
            return Result.error(MessageConstant.EDIT_FAIL);
        }

        return Result.success();
    }

    @Override
    @Transactional
    public Result<String> updateDish(DishDTO dishDTO) {
        //修改口味  -- 1.删除 2. 添加
        Long dishId = dishDTO.getId();
        //删除口味
        dishFlavorService.deleteFlavorsByDishId(dishId);

        List<DishFlavor> flavors = dishDTO.getFlavors();

        if (flavors != null && flavors.size() != 0) {
            //添加口味
            dishFlavorService.addDishFlavors(dishId, flavors);
        }

        //修改菜品
        Dish dish = new Dish();
        BeanUtils.copyProperties(dishDTO, dish);
        updateById(dish);

        return Result.success();
    }

    @Override
    @Transactional
    public Result<String> removeDishById(String ids) {

        //分割ids
        List<String> idList = Arrays.stream(ids.split(",")).collect(Collectors.toList());

        for (String id : idList) {
            //删除菜品
            removeById(id);
            //删除口味
            dishFlavorService.deleteFlavorsByDishId(Long.valueOf(id));
        }
        return Result.success();
    }

    @Override
    public Result<List<DishVO>> getDishVOByCategoryId(Long categoryId) {
        return Result.success(baseMapper.getDishesByCategoryId(categoryId));
    }
}
