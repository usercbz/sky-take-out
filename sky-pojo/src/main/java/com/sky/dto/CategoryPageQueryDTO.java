package com.sky.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

@Data
@ApiModel(description = "查询分类分页传递的数据模型")
public class CategoryPageQueryDTO implements Serializable {

    @ApiModelProperty(value = "页码",required = true)
    private int page;

    @ApiModelProperty(value = "每页记录数",required = true)
    private int pageSize;

    @ApiModelProperty("分类名称")
    private String name;

    @ApiModelProperty("分类类型 1菜品分类  2套餐分类")
    private Integer type;

}
