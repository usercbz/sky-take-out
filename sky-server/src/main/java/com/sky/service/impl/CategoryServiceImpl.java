package com.sky.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.sky.constant.MessageConstant;
import com.sky.dto.CategoryDTO;
import com.sky.dto.CategoryPageQueryDTO;
import com.sky.entity.Category;
import com.sky.entity.Dish;
import com.sky.entity.Setmeal;
import com.sky.exception.DeletionNotAllowedException;
import com.sky.exception.EditFailedException;
import com.sky.exception.SaveFailedException;
import com.sky.mapper.CategoryMapper;
import com.sky.result.PageResult;
import com.sky.service.CategoryService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

import static com.sky.constant.MessageConstant.CATEGORY_BE_RELATED_BY_SETMEAL_OR_DISH;
import static com.sky.constant.MessageConstant.UNKNOWN_ERROR;

@Service
public class CategoryServiceImpl extends ServiceImpl<CategoryMapper, Category> implements CategoryService {

    @Autowired
    private DishServiceImpl dishService;
    @Autowired
    private SetmealServiceImpl setmealService;

    /**
     * 分类查询分页
     *
     * @param categoryPageQueryDTO
     * @return
     */
    @Override
    public PageResult queryPage(CategoryPageQueryDTO categoryPageQueryDTO) {
        String name = categoryPageQueryDTO.getName();
        Integer type = categoryPageQueryDTO.getType();

        LambdaQueryWrapper<Category> queryWrapper = null;

        //封装查询条件
        if (name != null) {
            queryWrapper = new LambdaQueryWrapper<>();
            queryWrapper.like(Category::getName, name);
        }
        if (type != null) {
            if (queryWrapper == null) {
                queryWrapper = new LambdaQueryWrapper<>();
            }
            queryWrapper.eq(Category::getType, type);
        }

        //查询分页数据
        Page<Category> categoryPage = page(new Page<>(categoryPageQueryDTO.getPage(), categoryPageQueryDTO.getPageSize()), queryWrapper);

        return new PageResult(categoryPage.getTotal(), categoryPage.getRecords());
    }

    /**
     * 添加分类
     *
     * @param categoryDTO
     * @return
     */
    @Override
    public void addCategory(CategoryDTO categoryDTO) {

        Category category = new Category();
        //拷贝属性
        BeanUtils.copyProperties(categoryDTO, category);

        if (!save(category)) {
            throw new SaveFailedException(MessageConstant.ADD_FAIL);
        }

    }

    @Override
    public List<Category> queryListByType(Integer type) {

        LambdaQueryWrapper<Category> categoryLambdaQueryWrapper = null;
        if (type != null) {
            categoryLambdaQueryWrapper = new LambdaQueryWrapper<>();
            categoryLambdaQueryWrapper.eq(Category::getType, type);
        }
        return list(categoryLambdaQueryWrapper);
    }

    @Override
    public void updateStatusById(Integer status, Long id) {
        Category category = new Category();
        category.setId(id);
        category.setStatus(status);

        if (!updateById(category)) {
            throw new EditFailedException(MessageConstant.EDIT_FAIL);
        }
    }

    @Override
    public void updateCategory(CategoryDTO categoryDTO) {

        Category category = new Category();
        BeanUtils.copyProperties(categoryDTO, category);

        if (!updateById(category)) {
            throw new EditFailedException(MessageConstant.EDIT_FAIL);
        }
    }

    @Override
    public void removeCategoryById(Long id) {
        //查询是否有菜品或套餐有对它的引用
        List<Dish> dishes = dishService.getDishByCategoryId(id);
        List<Setmeal> setmeals = setmealService.getSetmealByCategoryId(id);

        if (dishes.size() != 0 || setmeals.size() != 0) {
            throw new DeletionNotAllowedException(CATEGORY_BE_RELATED_BY_SETMEAL_OR_DISH);
        }

        //删除
        if (!removeById(id)) {
            throw new DeletionNotAllowedException(UNKNOWN_ERROR);
        }
    }

}
