package com.sky.controller.admin;

import com.sky.dto.DataOverViewQueryDTO;
import com.sky.result.Result;
import com.sky.service.ReportService;
import com.sky.vo.OrderReportVO;
import com.sky.vo.SalesTop10ReportVO;
import com.sky.vo.TurnoverReportVO;
import com.sky.vo.UserReportVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@Slf4j
@RequestMapping("/admin/report")
public class ReportController {

    @Autowired
    private ReportService reportService;

    /**
     * 统计营业金额
     *
     * @return 营业数据
     */
    @GetMapping("turnoverStatistics")
    public Result<TurnoverReportVO> turnoverStatistics(DataOverViewQueryDTO dataOverViewQueryDTO) {
        TurnoverReportVO reportVO = reportService.turnoverStatistics(dataOverViewQueryDTO);
        return Result.success(reportVO);
    }

    /**
     * 订单统计
     *
     * @return
     */
    @GetMapping("ordersStatistics")
    public Result<OrderReportVO> ordersStatistics(DataOverViewQueryDTO dataOverViewQueryDTO) {
        OrderReportVO orderReportVO = reportService.ordersStatistics(dataOverViewQueryDTO);
        return Result.success(orderReportVO);
    }

    /**
     * 用户统计
     *
     * @return
     */
    @GetMapping("userStatistics")
    public Result<UserReportVO> userStatistics(DataOverViewQueryDTO dataOverViewQueryDTO) {
        UserReportVO userReportVO = reportService.userStatistics(dataOverViewQueryDTO);
        return Result.success(userReportVO);
    }

    /**
     *
     * @param dataOverViewQueryDTO
     * @return
     */
    @GetMapping("top10")
    public Result<SalesTop10ReportVO> salesTop10Report(DataOverViewQueryDTO dataOverViewQueryDTO) {
        SalesTop10ReportVO salesTop10ReportVO = reportService.salesTop10Report(dataOverViewQueryDTO);
        return Result.success(salesTop10ReportVO);
    }

    @GetMapping("export")
    public Result<Object> export() {
//        reportService.export();
        return Result.success();
    }

}
