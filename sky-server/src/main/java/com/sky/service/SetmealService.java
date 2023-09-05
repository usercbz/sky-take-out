package com.sky.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.sky.entity.Setmeal;

import java.util.List;

public interface SetmealService extends IService<Setmeal> {

    List<Setmeal> getSetmealByCategoryId(Long categoryId);
}
