package com.sky.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.sky.dto.DishDTO;
import com.sky.dto.DishPageQueryDTO;
import com.sky.entity.Dish;
import com.sky.result.PageResult;
import com.sky.vo.DishVO;

import java.util.List;

public interface DishService extends IService<Dish> {

    /**
     * 根据 分类查询菜品
     *
     * @param categoryId 分类id
     * @return 菜品集合
     */
    List<Dish> getDishByCategoryId(Long categoryId);

    /**
     * 查询菜品分页
     *
     * @param dishPageQueryDTO 查询条件、分页配置
     * @return 菜品分页信息
     */
    PageResult queryDishPage(DishPageQueryDTO dishPageQueryDTO);

    /**
     * 查询菜品信息
     *
     * @param id 菜品id
     * @return 菜品视图
     */
    DishVO queryDishById(Long id);

    /**
     * 新增菜品
     *
     * @param dishDTO 菜品数据
     */
    void addDish(DishDTO dishDTO);

    /**
     * 菜品 起售、停售
     *
     * @param id     菜品id
     * @param status 状态
     */
    void updateStatusById(Long id, Integer status);

    /**
     * 修改菜品信息
     *
     * @param dishDTO 修改后菜品信息
     */
    void updateDish(DishDTO dishDTO);

    /**
     * 删除菜品
     *
     * @param ids 待删除id序列
     */
    void removeDishById(String ids);

    /**
     * 获取菜品视图 集合
     *
     * @param categoryId 分类id
     * @return 菜品视图集合数据
     */
    List<DishVO> getDishVOByCategoryId(Long categoryId);
}
