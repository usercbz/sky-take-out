package com.sky.service.impl;

import com.sky.constant.MessageConstant;
import com.sky.dto.DataOverViewQueryDTO;
import com.sky.dto.GoodsSalesDTO;
import com.sky.entity.Orders;
import com.sky.entity.User;
import com.sky.exception.BaseException;
import com.sky.service.*;
import com.sky.vo.*;
import org.apache.commons.lang.StringUtils;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class ReportServiceImpl implements ReportService {

    @Autowired
    private OrderService orderService;
    @Autowired
    private UserService userService;
    @Autowired
    private WorkspaceService workspaceService;

    @Override
    public TurnoverReportVO turnoverStatistics(DataOverViewQueryDTO dataOverViewQueryDTO) {

        LocalDate begin = dataOverViewQueryDTO.getBegin();
        LocalDate end = dataOverViewQueryDTO.getEnd();

        //构建日期、金额集合
        List<LocalDate> dateList = new ArrayList<>();
        List<Double> turnoverList = new ArrayList<>();
        LocalDate endDate = end.plusDays(1);

        while (endDate.isAfter(begin)) {
            //日期集合
            dateList.add(begin);
            LocalDateTime dateBegin = LocalDateTime.of(begin, LocalTime.MIN);
            LocalDateTime dateEnd = LocalDateTime.of(begin, LocalTime.MAX);
            //统计金额
            // select sum(amount) from orders where status = 5 and order_time between ? and ?
            Double amount = orderService.getAmountCount(dateBegin, dateEnd);
            turnoverList.add(amount == null ? 0 : amount);
            //天数 + 1
            begin = begin.plusDays(1);
        }
        //返回VO数据
        return new TurnoverReportVO(StringUtils.join(dateList, ','), StringUtils.join(turnoverList, ','));
    }


    @Override
    public OrderReportVO ordersStatistics(DataOverViewQueryDTO dataOverViewQueryDTO) {

        LocalDate begin = dataOverViewQueryDTO.getBegin();
        LocalDate end = dataOverViewQueryDTO.getEnd();

        //日期、每日订单数、每日有效订单数
        List<LocalDate> dateList = new ArrayList<>();
        List<Integer> orderCountList = new ArrayList<>();
        List<Integer> validOrderCountList = new ArrayList<>();

        //循环统计、每天订单相关信息
        LocalDate endDate = end.plusDays(1);
        while (endDate.isAfter(begin)) {
            dateList.add(begin);
            LocalDateTime dateBegin = LocalDateTime.of(begin, LocalTime.MIN);
            LocalDateTime dateEnd = LocalDateTime.of(begin, LocalTime.MAX);
            //获取每日订单集合
            List<Orders> orders = orderService.getOrdersByDate(dateBegin, dateEnd);
            //每日的订单数量
            orderCountList.add(orders.size());
            //计算当天的有效订单数
            int validCount = (int) orders.stream()
                    .filter(order -> order.getStatus() == Orders.COMPLETED)
                    .count();
            //每日有效订单数
            validOrderCountList.add(validCount);
            //天数+1
            begin = begin.plusDays(1);
        }
        //订单总数
        Integer totalOrderCount = orderCountList.stream().reduce(Integer::sum).get();
        //有效订单数
        Integer validOrderCount = validOrderCountList.stream().reduce(Integer::sum).get();
        //订单完成率
        double orderCompletionRate;
        //计算完成率
        if (totalOrderCount == 0) {
            orderCompletionRate = 0D;
        } else {
            orderCompletionRate = (double) validOrderCount / (double) totalOrderCount;
        }
        return new OrderReportVO(StringUtils.join(dateList, ','), StringUtils.join(orderCountList, ','), StringUtils.join(validOrderCountList, ','), totalOrderCount, validOrderCount, orderCompletionRate);
    }


    @Override
    public UserReportVO userStatistics(DataOverViewQueryDTO dataOverViewQueryDTO) {

        //时间段
        LocalDate begin = dataOverViewQueryDTO.getBegin();
        LocalDate end = dataOverViewQueryDTO.getEnd();
        //日期
        List<LocalDate> dateList = new ArrayList<>();
        //用户总量
        List<Integer> totalUserList = new ArrayList<>();
        //新增用户
        List<Integer> newUserList = new ArrayList<>();

        //循环统计、每天用户相关信息
        LocalDate endDate = end.plusDays(1);
        while (endDate.isAfter(begin)) {

            dateList.add(begin);
            LocalDateTime dateBegin = LocalDateTime.of(begin, LocalTime.MIN);
            LocalDateTime dateEnd = LocalDateTime.of(begin, LocalTime.MAX);
            //当天新增的用户
            List<User> users = userService.getUsersByTime(dateBegin, dateEnd);
            newUserList.add(users.size());
            //当前的用户
            users = userService.getUsersByTime(null, dateEnd);
            totalUserList.add(users.size());
            //天数+1
            begin = begin.plusDays(1);
        }

        return new UserReportVO(
                StringUtils.join(dateList, ','),
                StringUtils.join(totalUserList, ','),
                StringUtils.join(newUserList, ','));
    }

    @Override
    public SalesTop10ReportVO salesTop10Report(DataOverViewQueryDTO dataOverViewQueryDTO) {

        //时间段
        LocalDate begin = dataOverViewQueryDTO.getBegin();
        LocalDate end = dataOverViewQueryDTO.getEnd();
        //商品名称 数量
        List<GoodsSalesDTO> goodsSalesDTOS = orderService.salesTop10Report(
                LocalDateTime.of(begin, LocalTime.MIN),
                LocalDateTime.of(end, LocalTime.MAX));
        //商品名称集合
        List<String> nameList = goodsSalesDTOS.stream()
                .map(GoodsSalesDTO::getName)
                .collect(Collectors.toList());
        //销量集合
        List<Integer> numberList = goodsSalesDTOS.stream()
                .map(GoodsSalesDTO::getNumber)
                .collect(Collectors.toList());

        return new SalesTop10ReportVO(
                StringUtils.join(nameList, ','),
                StringUtils.join(numberList, ','));
    }

    @Override
    public void export(HttpServletResponse response) {
        //获取近三十天营业数据
        LocalDate now = LocalDate.now();
        //三十天前
        LocalDate begin = now.minusDays(30);
        //昨天
        LocalDate end = now.minusDays(1);
        //运营数据概览
        BusinessDataVO businessDataVO = workspaceService.getBusinessData(
                LocalDateTime.of(begin, LocalTime.MIN),
                LocalDateTime.of(end, LocalTime.MAX));
        //数据模板
        InputStream template = getClass().getClassLoader()
                .getResourceAsStream("template/运营数据报表模板.xlsx");

        if (template == null) {
            throw new BaseException(MessageConstant.UNKNOWN_ERROR);
        }

        try {
            XSSFWorkbook excel = new XSSFWorkbook(template);
            XSSFSheet sheet = excel.getSheet("Sheet1");
            //写入到Excel文件中
            //填充时间段
            sheet.getRow(1).getCell(1).setCellValue("时间：" + begin + "至" + end);
            //填充概览数据
            XSSFRow row = sheet.getRow(3);//第四行
            row.getCell(2).setCellValue(businessDataVO.getTurnover());//填充营业额
            row.getCell(4).setCellValue(businessDataVO.getOrderCompletionRate());//订单完成率
            row.getCell(6).setCellValue(businessDataVO.getNewUsers());//新增用户数
            //第五行
            row = sheet.getRow(4);
            row.getCell(2).setCellValue(businessDataVO.getValidOrderCount());//有效订单
            row.getCell(4).setCellValue(businessDataVO.getUnitPrice());//平均客单价

            //循环写入30天的详细运营数据
            for (int i = 0; i < 30; i++) {
                //相应日期的营业数据
                BusinessDataVO businessData = workspaceService.getBusinessData(
                        LocalDateTime.of(begin, LocalTime.MIN),
                        LocalDateTime.of(begin, LocalTime.MAX));
                row = sheet.getRow(i + 7);//8 ~ 37 行
                row.getCell(1).setCellValue(begin.toString());//填充日期
                row.getCell(2).setCellValue(businessData.getTurnover());//填充营业额
                row.getCell(3).setCellValue(businessData.getValidOrderCount());//有效订单
                row.getCell(4).setCellValue(businessData.getOrderCompletionRate());//订单完成率
                row.getCell(5).setCellValue(businessData.getUnitPrice());//平均客单价
                row.getCell(6).setCellValue(businessData.getNewUsers());//新增用户数
                begin = begin.plusDays(1);
            }
            //下载到客户端浏览器
            ServletOutputStream outputStream = response.getOutputStream();
            excel.write(outputStream);
            //刷新
            outputStream.flush();
            //关闭
            outputStream.close();
            excel.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
