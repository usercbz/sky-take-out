package com.sky.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.sky.dto.CategoryDTO;
import com.sky.dto.CategoryPageQueryDTO;
import com.sky.entity.Category;
import com.sky.result.PageResult;
import com.sky.result.Result;

import java.util.List;

public interface CategoryService extends IService<Category> {
    Result<PageResult> queryPage(CategoryPageQueryDTO categoryPageQueryDTO);

    Result<String> addCategory(CategoryDTO categoryDTO);

    Result<List<Category>> queryListByType(Integer type);

    Result<String> updateStatusById(Integer status, Long id);

    Result<String> updateCategory(CategoryDTO categoryDTO);

    Result<String> removeCategoryById(Long id);
}
