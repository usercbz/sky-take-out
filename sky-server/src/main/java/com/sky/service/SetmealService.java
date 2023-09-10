package com.sky.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.sky.dto.SetmealDTO;
import com.sky.dto.SetmealPageQueryDTO;
import com.sky.entity.Setmeal;
import com.sky.result.PageResult;
import com.sky.result.Result;
import com.sky.vo.DishItemVO;
import com.sky.vo.SetmealVO;

import java.util.List;

public interface SetmealService extends IService<Setmeal> {

    List<Setmeal> getSetmealByCategoryId(Long categoryId);

    PageResult queryPage(SetmealPageQueryDTO pageQueryDTO);

    SetmealVO querySetmealById(Long id);

    void saveSetmeal(SetmealDTO setmealDTO);

    void updateSetmeal(SetmealDTO setmealDTO);

    void updateStatus(Long id, Integer status);

    void removeSetmealByIds(String ids);

    List<DishItemVO> getSetmealDishItems(Long setmealId);
}
