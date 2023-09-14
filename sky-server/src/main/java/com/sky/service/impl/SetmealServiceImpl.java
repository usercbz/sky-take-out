package com.sky.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.sky.constant.MessageConstant;
import com.sky.constant.StatusConstant;
import com.sky.dto.SetmealDTO;
import com.sky.dto.SetmealPageQueryDTO;
import com.sky.entity.Category;
import com.sky.entity.Dish;
import com.sky.entity.Setmeal;
import com.sky.entity.SetmealDish;
import com.sky.exception.BaseException;
import com.sky.exception.DeletionNotAllowedException;
import com.sky.exception.SaveFailedException;
import com.sky.exception.SetmealEnableFailedException;
import com.sky.mapper.SetmealMapper;
import com.sky.result.PageResult;
import com.sky.service.CategoryService;
import com.sky.service.DishService;
import com.sky.service.SetmealDishService;
import com.sky.service.SetmealService;
import com.sky.vo.DishItemVO;
import com.sky.vo.SetmealVO;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
public class SetmealServiceImpl extends ServiceImpl<SetmealMapper, Setmeal> implements SetmealService {

    @Autowired
    private SetmealDishService setmealDishService;

    @Autowired
    private CategoryService categoryService;

    @Autowired
    private DishService dishService;

    @Override
    public List<Setmeal> getSetmealByCategoryId(Long categoryId) {
        LambdaQueryWrapper<Setmeal> wrapper = new LambdaQueryWrapper<Setmeal>()
                .eq(Setmeal::getCategoryId, categoryId);
        return list(wrapper);
    }

    @Override
    public PageResult queryPage(SetmealPageQueryDTO pageQueryDTO) {

        Integer categoryId = pageQueryDTO.getCategoryId();
        String name = pageQueryDTO.getName();
        Integer status = pageQueryDTO.getStatus();

        LambdaQueryWrapper<Setmeal> queryWrapper = new LambdaQueryWrapper<>();

        //封装条件
        if (name != null) queryWrapper.like(Setmeal::getName, name);

        if (categoryId != null) queryWrapper.eq(Setmeal::getCategoryId, categoryId);

        if (status != null) queryWrapper.eq(Setmeal::getStatus, status);
        //查询
        Page<Setmeal> setmealPage = page(new Page<>(pageQueryDTO.getPage(), pageQueryDTO.getPageSize()), queryWrapper);

        List<Setmeal> setmeals = setmealPage.getRecords();

        ArrayList<SetmealVO> setmealVOS = new ArrayList<>();

        if (setmeals != null) {
            for (Setmeal setmeal : setmeals) {
                Category category = categoryService.getById(setmeal.getCategoryId());
                SetmealVO setmealVO = new SetmealVO();
                BeanUtils.copyProperties(setmeal, setmealVO);
                //套餐名字
                setmealVO.setCategoryName(category.getName());
                //添加到列表
                setmealVOS.add(setmealVO);
            }
        }

        return new PageResult(setmealPage.getTotal(), setmealVOS);
    }

    @Override
    public SetmealVO querySetmealById(Long id) {

        Setmeal setmeal = getById(id);

        if (setmeal == null) {
            throw new BaseException(MessageConstant.UNKNOWN_ERROR);
        }
        SetmealVO setmealVO = new SetmealVO();
        BeanUtils.copyProperties(setmeal, setmealVO);
        //查询套餐中菜品
        setmealVO.setSetmealDishes(setmealDishService.queryDishBySetmealId(id));

        return setmealVO;
    }

    @Override
    @Transactional
    public void saveSetmeal(SetmealDTO setmealDTO) {

        Setmeal setmeal = new Setmeal();
        BeanUtils.copyProperties(setmealDTO, setmeal);
        //保存套餐
        if (!save(setmeal)) {
            throw new SaveFailedException(MessageConstant.ADD_FAIL);
        }
        Long setmealId = setmeal.getId();
        //保存套餐的菜品关系
        List<SetmealDish> setmealDishes = setmealDTO.getSetmealDishes();
        setmealDishService.saveDishesBySetmealId(setmealId, setmealDishes);

    }

    @Override
    @Transactional
    public void updateSetmeal(SetmealDTO setmealDTO) {

        Setmeal setmeal = new Setmeal();
        BeanUtils.copyProperties(setmealDTO, setmeal);
        //修改套餐
        updateById(setmeal);
        //修改套餐菜品关系: 1.删除套餐菜品关系，新增套餐菜品关系数据
        Long setmealId = setmealDTO.getId();
        //删除套餐中菜品
        setmealDishService.deleteDishBySetmealId(setmealId);

        List<SetmealDish> setmealDishes = setmealDTO.getSetmealDishes();
        //添加
        if (setmealDishes != null) {
            setmealDishService.saveDishesBySetmealId(setmealId, setmealDishes);
        }
    }

    @Override
    public void updateStatus(Long id, Integer status) {

        if (StatusConstant.ENABLE == status) {
            List<SetmealDish> setmealDishes = setmealDishService.queryDishBySetmealId(id);
            for (SetmealDish setmealDish : setmealDishes) {
                Dish dish = dishService.getById(setmealDish.getDishId());
                if (dish.getStatus() == StatusConstant.DISABLE) {
                    throw new SetmealEnableFailedException(MessageConstant.SETMEAL_ENABLE_FAILED);
                }
            }
        }
        updateById(Setmeal.builder()
                .id(id)
                .status(status)
                .build());
    }

    @Override
    @Transactional
    public void removeSetmealByIds(String ids) {
        //分割id
        for (String id : ids.split(",")) {

            //判断套餐状态
            if (getById(id).getStatus() == StatusConstant.ENABLE) {
                throw new DeletionNotAllowedException(MessageConstant.SETMEAL_ON_SALE);
            }
            //删除套餐
            removeById(id);
            //删除套餐菜品关系
            setmealDishService.deleteDishBySetmealId(Long.valueOf(id));
        }
    }

    @Override
    public List<DishItemVO> getSetmealDishItems(Long setmealId) {
        List<SetmealDish> setmealDishes = setmealDishService.queryDishBySetmealId(setmealId);
        ArrayList<DishItemVO> dishItemVOS = new ArrayList<>();

        for (SetmealDish setmealDish : setmealDishes) {
            Dish dish = dishService.getById(setmealDish.getDishId());
            DishItemVO dishItemVO = new DishItemVO();
            //拷贝属性
            BeanUtils.copyProperties(dish, dishItemVO);
            dishItemVO.setCopies(setmealDish.getCopies());

            dishItemVOS.add(dishItemVO);
        }
        return dishItemVOS;
    }
}
