package com.sky.controller.admin;

import com.sky.dto.DishDTO;
import com.sky.dto.DishPageQueryDTO;
import com.sky.entity.Dish;
import com.sky.result.PageResult;
import com.sky.result.Result;
import com.sky.service.DishService;
import com.sky.vo.DishVO;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("admin/dish")
public class DishController {

    private final DishService dishService;

    public DishController(DishService dishService) {
        this.dishService = dishService;
    }

    @RequestMapping("page")
    public Result<PageResult> getDishPage(DishPageQueryDTO dishPageQueryDTO) {
        PageResult dishPage = dishService.queryDishPage(dishPageQueryDTO);
        return Result.success(dishPage);
    }


    @GetMapping("/{id}")
    public Result<DishVO> getDishById(@PathVariable Long id) {
        DishVO dishVO = dishService.queryDishById(id);
        return Result.success(dishVO);
    }


    @GetMapping("list")
    public Result<List<Dish>> getDishListByCategory(Long categoryId) {
        List<Dish> dishes = dishService.getDishByCategoryId(categoryId);
        return Result.success(dishes);
    }


    @PostMapping
    public Result<String> addDish(@RequestBody DishDTO dishDTO) {
        dishService.addDish(dishDTO);
        return Result.success();
    }


    @PostMapping("status/{status}")
    public Result<String> editDishStatus(Long id, @PathVariable Integer status) {
        dishService.updateStatusById(id, status);
        return Result.success();
    }


    @PutMapping
    public Result<String> editDishInfo(@RequestBody DishDTO dishDTO) {
        dishService.updateDish(dishDTO);
        return Result.success();
    }

    @DeleteMapping
    public Result<String> deleteDish(String ids) {
        dishService.removeDishById(ids);
        return Result.success();
    }
}
