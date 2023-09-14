package com.sky.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.sky.dto.GoodsSalesDTO;
import com.sky.entity.Orders;

import java.time.LocalDateTime;
import java.util.List;

public interface OrderMapper extends BaseMapper<Orders> {

    List<GoodsSalesDTO> salesTop10Report(LocalDateTime begin, LocalDateTime end);

    Double getAmountCount(LocalDateTime begin, LocalDateTime end);
}
