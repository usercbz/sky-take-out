package com.sky.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.sky.dto.CategoryDTO;
import com.sky.dto.CategoryPageQueryDTO;
import com.sky.entity.Category;
import com.sky.result.PageResult;

import java.util.List;

public interface CategoryService extends IService<Category> {

    PageResult queryPage(CategoryPageQueryDTO categoryPageQueryDTO);

    void addCategory(CategoryDTO categoryDTO);

    List<Category> queryListByType(Integer type);

    void updateStatusById(Integer status, Long id);

    void updateCategory(CategoryDTO categoryDTO);

    void removeCategoryById(Long id);
}
