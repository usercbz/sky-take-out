package com.sky.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.sky.entity.Setmeal;
import com.sky.mapper.SetmealMapper;
import com.sky.service.SetmealService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SetmealServiceImpl extends ServiceImpl<SetmealMapper, Setmeal> implements SetmealService {
    @Override
    public List<Setmeal> getSetmealByCategoryId(Long categoryId) {
        LambdaQueryWrapper<Setmeal> wrapper = new LambdaQueryWrapper<Setmeal>().eq(Setmeal::getCategoryId, categoryId);
        return list(wrapper);
    }
}
