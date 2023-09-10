package com.sky.controller.admin;

import com.sky.dto.CategoryDTO;
import com.sky.dto.CategoryPageQueryDTO;
import com.sky.entity.Category;
import com.sky.result.PageResult;
import com.sky.result.Result;
import com.sky.service.CategoryService;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/admin/category")
public class CategoryController {

    @Autowired
    private CategoryService categoryService;


    /**
     * 分类查询分页
     *
     * @param categoryPageQueryDTO
     * @return
     */
    @GetMapping("page")
    @ApiOperation("分类查询分页")
    public Result<PageResult> getCategoryPage(CategoryPageQueryDTO categoryPageQueryDTO) {
        PageResult pageResult = categoryService.queryPage(categoryPageQueryDTO);
        return Result.success(pageResult);
    }

    /**
     * @param categoryDTO
     * @return
     */
    @PostMapping
    @ApiOperation("添加分类信息")
    public Result<String> addCategory(@RequestBody CategoryDTO categoryDTO) {
        categoryService.addCategory(categoryDTO);
        return Result.success();
    }


    /**
     * @param categoryDTO
     * @return
     */
    @PutMapping
    @ApiOperation("修改分类信息")
    public Result<String> editCategoryInfo(@RequestBody CategoryDTO categoryDTO) {
        categoryService.updateCategory(categoryDTO);
        return Result.success();
    }

    /**
     * @param status
     * @param id
     * @return
     */
    @PostMapping("status/{status}")
    @ApiOperation("修改状态")
    public Result<String> editCategoryStatusById(@PathVariable Integer status, Long id) {
        categoryService.updateStatusById(status,id);
        return Result.success();
    }

    /**
     * @param type
     * @return
     */
    @GetMapping("list")
    @ApiOperation("根据类型查询分类")
    public Result<List<Category>> getCategoryListByType(Integer type) {
        List<Category> categories = categoryService.queryListByType(type);
        return Result.success(categories);
    }

    @DeleteMapping
    @ApiOperation("根据id删除分类")
    public Result<String> deleteCategoryById(Long id){
        categoryService.removeCategoryById(id);
        return Result.success();
    }
}
