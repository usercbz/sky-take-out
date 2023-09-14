package com.sky.controller.user;

import com.sky.constant.StatusConstant;
import com.sky.entity.Category;
import com.sky.result.Result;
import com.sky.service.CategoryService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

@RestController("userCategoryController")
@RequestMapping("/user/category")
@Slf4j
public class CategoryController {

    @Autowired
    private CategoryService categoryService;

    @GetMapping("list")
    public Result<List<Category>> getCategoryList(Integer type) {
        List<Category> categories = categoryService.queryListByType(type);
        //过滤
        categories = categories.stream()
                .filter(category -> category.getStatus() == StatusConstant.ENABLE)
                .collect(Collectors.toList());
        return Result.success(categories);
    }
}
