package com.FCfactory.controller;

import com.FCfactory.common.R;
import com.FCfactory.entity.Category;
import com.FCfactory.service.CategoryService;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/category")
@Slf4j
public class CategoryController {
    @Autowired
    private CategoryService categoryService;

    /**
     * 新增操作
     * @param category
     * @return
     */
    @PostMapping
    public R<String> save(@RequestBody Category category){
        log.info(category.toString());
        categoryService.save(category);
        return R.success("添加成功");
    }

    /**
     * 分页查询
     * @param page 当前页码
     * @param pageSize 每页查询条数
     * @return
     */
    @GetMapping("/page")
    public R<Page> getByPage(int page,int pageSize){
        Page<Category> categoryPage = categoryService.getByPage(page, pageSize);
        return R.success(categoryPage);
    }

    /**
     * 删除 删除前查询是否存在关联
     * @param ids
     * @return
     */
    @DeleteMapping
    public R<String> delete(Long ids){
        categoryService.remove(ids);
        return R.success("删除菜品分类成功");
    }

    /**
     * 修改
     * @param category
     * @return
     */
    @PutMapping
    public R<String> update(@RequestBody  Category category){
        log.info(category.toString());
        categoryService.updateById(category);
        return R.success("修改成功");
    }

    @GetMapping("/list")
    public R<List<Category>> list(Category category){
        log.info(category.toString());
        List<Category> categoryList = categoryService.selectList(category);
        return R.success(categoryList);
    }
}
