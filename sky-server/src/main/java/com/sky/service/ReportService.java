package com.sky.service;

import com.sky.dto.DataOverViewQueryDTO;
import com.sky.vo.OrderReportVO;
import com.sky.vo.SalesTop10ReportVO;
import com.sky.vo.TurnoverReportVO;
import com.sky.vo.UserReportVO;


public interface ReportService {


    /**
     * 统计营业金额
     *
     * @param dataOverViewQueryDTO
     * @return
     */
    TurnoverReportVO turnoverStatistics(DataOverViewQueryDTO dataOverViewQueryDTO);

    /**
     * 订单统计
     *
     * @param dataOverViewQueryDTO
     * @return
     */
    OrderReportVO ordersStatistics(DataOverViewQueryDTO dataOverViewQueryDTO);

    /**
     * 用户统计
     *
     * @param dataOverViewQueryDTO
     * @return
     */
    UserReportVO userStatistics(DataOverViewQueryDTO dataOverViewQueryDTO);

    /**
     * 商品前十统计
     *
     * @param dataOverViewQueryDTO
     * @return
     */
    SalesTop10ReportVO salesTop10Report(DataOverViewQueryDTO dataOverViewQueryDTO);
}
