package com.sky.controller.user;

import com.sky.constant.StatusConstant;
import com.sky.entity.Dish;
import com.sky.entity.Setmeal;
import com.sky.entity.SetmealDish;
import com.sky.result.Result;
import com.sky.service.DishService;
import com.sky.service.SetmealDishService;
import com.sky.service.SetmealService;
import com.sky.vo.DishItemVO;
import com.sky.vo.SetmealOverViewVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

@RestController("userSetmealController")
@RequestMapping("/user/setmeal")
@Slf4j
public class SetmealController {

    @Autowired
    private SetmealService setmealService;

    @GetMapping("list")
    public Result<List<Setmeal>> getSetmealList(Long categoryId) {

        List<Setmeal> setmeals = setmealService.getSetmealByCategoryId(categoryId);
        //查询起售的套餐
        setmeals = setmeals.stream()
                .filter(setmeal -> StatusConstant.ENABLE.equals(setmeal.getStatus()))
                .collect(Collectors.toList());
        return Result.success(setmeals);
    }

    @GetMapping("dish/{id}")
    public Result<List<DishItemVO>> getDishItems(@PathVariable Long id) {

        List<DishItemVO> dishItemVOS = setmealService.getSetmealDishItems(id);

        return Result.success(dishItemVOS);
    }
}
