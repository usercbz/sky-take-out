package com.sky.controller.admin;

import com.sky.dto.SetmealDTO;
import com.sky.dto.SetmealPageQueryDTO;
import com.sky.result.PageResult;
import com.sky.result.Result;
import com.sky.service.SetmealService;
import com.sky.vo.SetmealVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/admin/setmeal")
@Slf4j
public class SetmealController {

    @Autowired
    private SetmealService setmealService;

    /**
     * 套餐分页
     *
     * @param pageQueryDTO
     * @return
     */
    @GetMapping("page")
    public Result<PageResult> getSetmealPage(SetmealPageQueryDTO pageQueryDTO) {
        return setmealService.queryPage(pageQueryDTO);
    }

    /**
     * 根据id查询套餐
     *
     * @param id
     * @return
     */
    @GetMapping("/{id}")
    public Result<SetmealVO> getSetmealById(@PathVariable Long id) {
        return setmealService.querySetmealById(id);
    }

    /**
     * 新增套餐
     *
     * @param setmealDTO
     * @return
     */
    @PostMapping
    public Result<Object> addSetmeal(@RequestBody SetmealDTO setmealDTO) {
        return setmealService.saveSetmeal(setmealDTO);
    }

    /**
     * 修改套餐信息
     *
     * @param setmealDTO
     * @return
     */
    @PutMapping
    public Result<Object> editSetmealInfo(@RequestBody SetmealDTO setmealDTO) {
        return setmealService.updateSetmeal(setmealDTO);
    }

    /**
     * 套餐的起售、停售
     * @param id
     * @param status
     * @return
     */
    @PostMapping("status/{status}")
    public Result<Object> editSetmealStatus(Long id, @PathVariable Integer status) {
        return setmealService.updateStatus(id, status);
    }

    /**
     * 删除套餐
     * @param ids
     * @return
     */
    @DeleteMapping
    public Result<Object> deleteSetmealByIds(String ids){
        return setmealService.removeSetmealByIds(ids);
    }
}
