package com.sky.controller.user;

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

import java.util.ArrayList;
import java.util.List;

@RestController("userSetmealController")
@RequestMapping("/user/setmeal")
@Slf4j
public class SetmealController {

    @Autowired
    private SetmealService setmealService;

    @Autowired
    private SetmealDishService setmealDishService;

    @Autowired
    private DishService dishService;

    @GetMapping("list")
    public Result<List<Setmeal>> getSetmealList(Long categoryId) {

        List<Setmeal> setmeals = setmealService.getSetmealByCategoryId(categoryId);
        return Result.success(setmeals);
    }

    @GetMapping("dish/{id}")
    public Result<List<DishItemVO>> getDishItems(@PathVariable Long id) {
        List<SetmealDish> setmealDishes = setmealDishService.queryDishBySetmealId(id);
        ArrayList<DishItemVO> dishItemVOS = new ArrayList<>();

        for (SetmealDish setmealDish : setmealDishes) {
            Dish dish = dishService.getById(setmealDish.getDishId());
            DishItemVO dishItemVO = new DishItemVO();
            //拷贝属性
            BeanUtils.copyProperties(dish, dishItemVO);
            dishItemVO.setCopies(setmealDish.getCopies());

            dishItemVOS.add(dishItemVO);
        }

        return Result.success(dishItemVOS);
    }
}
