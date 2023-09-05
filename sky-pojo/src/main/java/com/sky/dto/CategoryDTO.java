package com.sky.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

@Data
@ApiModel(description = "分类传递的数据模型")
public class CategoryDTO implements Serializable {


    @ApiModelProperty(value = "主键")
    private Long id;

    @ApiModelProperty(value = "类型 1:菜品分类 、2:套餐分类",required = true)
    private Integer type;

    //分类名称
    @ApiModelProperty(value = "分类名称",required = true)
    private String name;

    //排序
    @ApiModelProperty(value = "排序")
    private Integer sort;

}
