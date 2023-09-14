package com.sky.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.sky.dto.SetmealDTO;
import com.sky.dto.SetmealPageQueryDTO;
import com.sky.entity.Setmeal;
import com.sky.result.PageResult;
import com.sky.vo.DishItemVO;
import com.sky.vo.SetmealVO;

import java.util.List;

public interface SetmealService extends IService<Setmeal> {

    /**
     * 根据分类获取套餐
     *
     * @param categoryId 套餐id
     * @return 套餐集合
     */
    List<Setmeal> getSetmealByCategoryId(Long categoryId);

    /**
     * 套餐分页信息
     *
     * @param pageQueryDTO 查询条件、分页配置
     * @return 分页信息
     */
    PageResult queryPage(SetmealPageQueryDTO pageQueryDTO);

    /**
     * 查询套餐详情
     *
     * @param id 套餐id
     * @return 套餐视图
     */
    SetmealVO querySetmealById(Long id);

    /**
     * 新增套餐
     *
     * @param setmealDTO 套餐信息
     */
    void saveSetmeal(SetmealDTO setmealDTO);

    /**
     * 修改套餐信息
     *
     * @param setmealDTO 修改后套餐信息
     */
    void updateSetmeal(SetmealDTO setmealDTO);

    /**
     * 套餐的起售、停售
     *
     * @param id     套餐id
     * @param status 状态
     */
    void updateStatus(Long id, Integer status);

    /**
     * 删除套餐
     *
     * @param ids 待删除套餐id序列
     */
    void removeSetmealByIds(String ids);

    /**
     * 获取套餐中包含的菜品
     *
     * @param setmealId 套餐id
     * @return 套餐包含得菜品数据
     */
    List<DishItemVO> getSetmealDishItems(Long setmealId);
}
