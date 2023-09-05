package com.sky.controller.admin;

import com.sky.dto.CategoryDTO;
import com.sky.dto.CategoryPageQueryDTO;
import com.sky.entity.Category;
import com.sky.result.PageResult;
import com.sky.result.Result;
import com.sky.service.impl.CategoryServiceImpl;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/admin/category")
@Slf4j
public class CategoryController {

    @Autowired
    private CategoryServiceImpl categoryService;


    /**
     * 分类查询分页
     *
     * @param categoryPageQueryDTO
     * @return
     */
    @GetMapping("page")
    @ApiOperation("分类查询分页")
    public Result<PageResult> getCategoryPage(CategoryPageQueryDTO categoryPageQueryDTO) {
        log.info("分类分页数据 {}", categoryPageQueryDTO);
        return categoryService.queryPage(categoryPageQueryDTO);
    }

    /**
     * @param categoryDTO
     * @return
     */
    @PostMapping
    @ApiOperation("添加分类信息")
    public Result<String> addCategory(@RequestBody CategoryDTO categoryDTO) {
        return categoryService.addCategory(categoryDTO);
    }


    /**
     *
     * @param categoryDTO
     * @return
     */
    @PutMapping
    @ApiOperation("修改分类信息")
    public Result<String> editCategoryInfo(@RequestBody CategoryDTO categoryDTO) {
        return categoryService.updateCategory(categoryDTO);
    }

    /**
     *
     * @param status
     * @param id
     * @return
     */
    @PostMapping("status/{status}")
    @ApiOperation("修改状态")
    public Result<String> editCategoryStatusById(@PathVariable Integer status, Long id) {
        return categoryService.updateStatusById(status,id);
    }

    /**
     *
     * @param type
     * @return
     */
    @GetMapping("list")
    @ApiOperation("根据类型查询分类")
    public Result<List<Category>> getCategoryListByType(Integer type) {
        return categoryService.queryListByType(type);
    }

    @DeleteMapping
    @ApiOperation("根据id删除分类")
    public Result<String> deleteCategoryById(Long id){
        return categoryService.removeCategoryById(id);
    }
}
