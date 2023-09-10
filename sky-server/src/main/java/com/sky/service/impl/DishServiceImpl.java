package com.sky.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.sky.constant.MessageConstant;
import com.sky.constant.StatusConstant;
import com.sky.dto.DishDTO;
import com.sky.dto.DishPageQueryDTO;
import com.sky.entity.Dish;
import com.sky.entity.DishFlavor;
import com.sky.exception.EditFailedException;
import com.sky.exception.SaveFailedException;
import com.sky.mapper.DishMapper;
import com.sky.result.PageResult;
import com.sky.service.CategoryService;
import com.sky.service.DishFlavorService;
import com.sky.service.DishService;
import com.sky.vo.DishVO;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static com.sky.constant.RedisConstant.DISH_CACHE_KEY;

@Service
public class DishServiceImpl extends ServiceImpl<DishMapper, Dish> implements DishService {

    @Autowired
    private DishFlavorService dishFlavorService;

    @Autowired
    private CategoryService categoryService;

    @Override
    public List<Dish> getDishByCategoryId(Long categoryId) {
        LambdaQueryWrapper<Dish> wrapper = new LambdaQueryWrapper<Dish>().eq(Dish::getCategoryId, categoryId);
        return list(wrapper);
    }

    @Override
    public PageResult queryDishPage(DishPageQueryDTO dishPageQueryDTO) {

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


        return new PageResult(page.getTotal(), page.getRecords());
    }

    @Override
    public DishVO queryDishById(Long id) {
        //查询菜品数据
        Dish dish = getById(id);
        return getDishVO(dish);
    }

    @Override
    public List<Dish> queryDishListByCategoryId(Long categoryId) {
        return list(Wrappers.lambdaQuery(Dish.class).eq(Dish::getCategoryId, categoryId));
    }


    @Override
    @Transactional
    @CacheEvict(cacheNames = DISH_CACHE_KEY, key = "#dishDTO.categoryId")
    public void addDish(DishDTO dishDTO) {

        //创建菜品
        Dish dish = new Dish();
        BeanUtils.copyProperties(dishDTO, dish);

        //保存菜品
        if (!save(dish)) {
            //保存失败
            throw new SaveFailedException(MessageConstant.ADD_FAIL);
        }
        //成功，取出保存后菜品id
        Long dishId = dish.getId();
        //创建菜品口味
        List<DishFlavor> flavors = dishDTO.getFlavors();

        //添加菜品口味
        dishFlavorService.addDishFlavors(dishId, flavors);

    }

    @Override
    @CacheEvict(cacheNames = DISH_CACHE_KEY, allEntries = true)
    public void updateStatusById(Long id, Integer status) {

        Dish dish = Dish.builder()
                .id(id)
                .status(status)
                .build();

        if (!updateById(dish)) {
            //失败
            throw new EditFailedException(MessageConstant.EDIT_FAIL);
        }

    }

    @Override
    @Transactional
    @CacheEvict(cacheNames = DISH_CACHE_KEY, allEntries = true)
    public void updateDish(DishDTO dishDTO) {
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

    }

    @Override
    @Transactional
    @CacheEvict(cacheNames = DISH_CACHE_KEY, allEntries = true)
    public void removeDishById(String ids) {

        //分割ids
        List<String> idList = Arrays.stream(ids.split(",")).collect(Collectors.toList());

        for (String id : idList) {
            //删除菜品
            removeById(id);
            //删除口味
            dishFlavorService.deleteFlavorsByDishId(Long.valueOf(id));
        }
    }

    @Override
    @Cacheable(cacheNames = DISH_CACHE_KEY, key = "#categoryId")
    public List<DishVO> getDishVOByCategoryId(Long categoryId) {
        //根据categoryId查询dish集合
        List<Dish> dishes = queryDishListByCategoryId(categoryId)
                .stream()
                .filter(dish -> StatusConstant.ENABLE.equals(dish.getStatus()))
                .collect(Collectors.toList());
        //构建dishVO
        ArrayList<DishVO> list = new ArrayList<>();
        for (Dish dish : dishes) {
            DishVO dishVO = getDishVO(dish);
            list.add(dishVO);
        }
        return list;
    }

    /**
     * 构建dishVO
     *
     * @param dish
     * @return
     */
    private DishVO getDishVO(Dish dish) {
        //获取菜品口味
        List<DishFlavor> flavors = dishFlavorService.getDishFlavorByDishId(dish.getId());
        DishVO dishVO = new DishVO();
        BeanUtils.copyProperties(dish, dishVO);
        dishVO.setFlavors(flavors);
        String categoryName = categoryService.getById(dish.getCategoryId()).getName();
        dishVO.setCategoryName(categoryName);

        return dishVO;
    }

}
