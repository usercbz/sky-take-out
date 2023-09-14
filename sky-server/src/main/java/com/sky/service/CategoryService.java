package com.sky.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.sky.dto.CategoryDTO;
import com.sky.dto.CategoryPageQueryDTO;
import com.sky.entity.Category;
import com.sky.result.PageResult;

import java.util.List;

public interface CategoryService extends IService<Category> {

    /**
     * 查询分类分页信息
     *
     * @param categoryPageQueryDTO 查询条件、分页配置
     * @return 分类分页信息
     */
    PageResult queryPage(CategoryPageQueryDTO categoryPageQueryDTO);

    /**
     * 新增分类
     *
     * @param categoryDTO 分类信息
     */
    void addCategory(CategoryDTO categoryDTO);

    /**
     * 根据类型查询分类
     *
     * @param type 类型
     * @return 分类信息
     */
    List<Category> queryListByType(Integer type);

    /**
     * 启用、禁用分类
     *
     * @param status 状态
     * @param id     分类id
     */
    void updateStatusById(Integer status, Long id);

    /**
     * 修改分类信息
     * @param categoryDTO 修改后分类信息
     */
    void updateCategory(CategoryDTO categoryDTO);

    /**
     * 删除分类
     *
     * @param id 分类id
     */
    void removeCategoryById(Long id);
}
