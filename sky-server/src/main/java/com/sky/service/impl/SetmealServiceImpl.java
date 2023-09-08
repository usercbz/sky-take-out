package com.sky.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.sky.constant.MessageConstant;
import com.sky.dto.SetmealDTO;
import com.sky.dto.SetmealPageQueryDTO;
import com.sky.entity.Category;
import com.sky.entity.Setmeal;
import com.sky.entity.SetmealDish;
import com.sky.mapper.SetmealMapper;
import com.sky.result.PageResult;
import com.sky.result.Result;
import com.sky.service.CategoryService;
import com.sky.service.SetmealDishService;
import com.sky.service.SetmealService;
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

    @Override
    public List<Setmeal> getSetmealByCategoryId(Long categoryId) {
        LambdaQueryWrapper<Setmeal> wrapper = new LambdaQueryWrapper<Setmeal>().eq(Setmeal::getCategoryId, categoryId);
        return list(wrapper);
    }

    @Override
    public Result<PageResult> queryPage(SetmealPageQueryDTO pageQueryDTO) {

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

        return Result.success(new PageResult(setmealPage.getTotal(), setmealVOS));
    }

    @Override
    public Result<SetmealVO> querySetmealById(Long id) {

        Setmeal setmeal = getById(id);

        if (setmeal == null) {
            return Result.error(MessageConstant.UNKNOWN_ERROR);
        }
        SetmealVO setmealVO = new SetmealVO();
        BeanUtils.copyProperties(setmeal, setmealVO);
        //查询套餐中菜品
        setmealVO.setSetmealDishes(setmealDishService.queryDishBySetmealId(id));

        return Result.success(setmealVO);
    }

    @Override
    @Transactional
    public Result<Object> saveSetmeal(SetmealDTO setmealDTO) {

        Setmeal setmeal = new Setmeal();
        BeanUtils.copyProperties(setmealDTO, setmeal);
        //保存套餐
        if (!save(setmeal)) {
            return Result.error(MessageConstant.ADD_FAIL);
        }
        Long setmealId = setmeal.getId();
        //保存套餐的菜品关系
        List<SetmealDish> setmealDishes = setmealDTO.getSetmealDishes();
        setmealDishService.saveDishesBySetmealId(setmealId, setmealDishes);

        return Result.success();
    }

    @Override
    @Transactional
    public Result<Object> updateSetmeal(SetmealDTO setmealDTO) {

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
        return Result.success();
    }

    @Override
    public Result<Object> updateStatus(Long id, Integer status) {
        updateById(Setmeal.builder()
                .id(id)
                .status(status)
                .build());
        return Result.success();
    }

    @Override
    @Transactional
    public Result<Object> removeSetmealByIds(String ids) {
        //分割id
        for (String id : ids.split(",")) {
            //删除套餐
            removeById(id);
            //删除套餐菜品关系
            setmealDishService.deleteDishBySetmealId(Long.valueOf(id));
        }
        return Result.success();
    }
}
