package com.sky.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.sky.dto.SetmealDTO;
import com.sky.dto.SetmealPageQueryDTO;
import com.sky.entity.Setmeal;
import com.sky.result.PageResult;
import com.sky.result.Result;
import com.sky.vo.SetmealVO;

import java.util.List;

public interface SetmealService extends IService<Setmeal> {

    List<Setmeal> getSetmealByCategoryId(Long categoryId);

    Result<PageResult> queryPage(SetmealPageQueryDTO pageQueryDTO);

    Result<SetmealVO> querySetmealById(Long id);

    Result<Object> saveSetmeal(SetmealDTO setmealDTO);

    Result<Object> updateSetmeal(SetmealDTO setmealDTO);

    Result<Object> updateStatus(Long id, Integer status);

    Result<Object> removeSetmealByIds(String ids);
}
