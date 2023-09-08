package com.sky.service.impl;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.sky.entity.SetmealDish;
import com.sky.mapper.SetmealDishMapper;
import com.sky.service.SetmealDishService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SetmealDishServiceImpl extends ServiceImpl<SetmealDishMapper, SetmealDish> implements SetmealDishService {
    @Override
    public List<SetmealDish> queryDishBySetmealId(Long setmealId) {
        return list(Wrappers.lambdaQuery(SetmealDish.class).eq(SetmealDish::getSetmealId, setmealId));
    }

    @Override
    public void saveDishesBySetmealId(Long setmealId, List<SetmealDish> setmealDishes) {
        for (SetmealDish setmealDish : setmealDishes) {
            setmealDish.setId(null);
            setmealDish.setSetmealId(setmealId);
            //保存
            save(setmealDish);
        }
    }

    @Override
    public void deleteDishBySetmealId(Long setmealId) {
        remove(Wrappers.lambdaQuery(SetmealDish.class)
                .eq(SetmealDish::getSetmealId, setmealId));
    }
}
