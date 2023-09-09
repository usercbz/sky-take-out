package com.sky.controller.user;

import com.sky.result.Result;
import com.sky.service.DishService;
import com.sky.vo.DishVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController("userDishController")
@RequestMapping("user/dish")
public class DishController {

    @Autowired
    private DishService dishService;

    @RequestMapping("list")
    public Result<List<DishVO>> browseDish(Long categoryId) {
        return dishService.getDishVOByCategoryId(categoryId);
    }


}
