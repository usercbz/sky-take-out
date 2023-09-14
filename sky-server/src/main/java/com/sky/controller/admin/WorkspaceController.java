package com.sky.controller.admin;

import com.sky.result.Result;
import com.sky.service.WorkspaceService;
import com.sky.vo.BusinessDataVO;
import com.sky.vo.DishOverViewVO;
import com.sky.vo.OrderOverViewVO;
import com.sky.vo.SetmealOverViewVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Slf4j
@RequestMapping("/admin/workspace")
public class WorkspaceController {

    @Autowired
    private WorkspaceService workspaceService;

    /**
     * 查询今日运营数据
     *
     * @return
     */
    @GetMapping("businessData")
    public Result<BusinessDataVO> getBusinessData() {
        BusinessDataVO businessDataVO = workspaceService.getBusinessData();
        return Result.success(businessDataVO);
    }

    @GetMapping("overviewOrders")
    public Result<OrderOverViewVO> getOverviewOrders() {
        OrderOverViewVO overViewVO = workspaceService.getOverviewOrders();
        return Result.success(overViewVO);
    }

    @GetMapping("overviewDishes")
    public Result<DishOverViewVO> getOverviewDishes() {
        DishOverViewVO dishOverViewVO =  workspaceService.getOverviewDishes();
        return Result.success(dishOverViewVO);
    }

    @GetMapping("overviewSetmeals")
    public Result<SetmealOverViewVO> getOverviewSetmeal() {
        SetmealOverViewVO setmealOverViewVO= workspaceService.getOverviewSetmeal();
        return Result.success(setmealOverViewVO);
    }
}
