package com.FCfactory.controller;

import com.FCfactory.common.R;
import com.FCfactory.dto.DishDto;
import com.FCfactory.entity.Dish;
import com.FCfactory.service.DishFlavorService;
import com.FCfactory.service.DishService;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/dish")
@Slf4j
public class DishController {
    @Autowired
    private DishService dishService;

    @Autowired
    private DishFlavorService dishFlavorService;


    /**
     * 添加菜品和口味 同时操作两张表
     *
     * @param dishDto
     * @return
     */
    @PostMapping()
    public R<String> addDish(@RequestBody DishDto dishDto) {
        log.info(dishDto.toString());
        dishService.addDishWithFlavor(dishDto);
        return R.success("新增菜品成功");
    }

    /**
     * 分页查询
     * @param page
     * @param pageSize
     * @param name
     * @return
     */
    @GetMapping("/page")
    public R<Page> getByPage(int page,int pageSize,String name){
        Page<DishDto> dishDtoPage = dishService.getByPage(page, pageSize, name);
        return R.success(dishDtoPage);
    }

    /**
     * 数据回显
     * @param id
     * @return
     */
    @GetMapping("/{id}")
    public R<DishDto> getEditData(@PathVariable Long id){
        DishDto editData = dishService.getEditData(id);
        return R.success(editData);
    }

    /**
     * 修改菜品
     * @param dishDto
     * @return
     */
    @PutMapping
    public R<String> update(@RequestBody DishDto dishDto){
        log.info(dishDto.toString());
        dishService.updateWithFlavor(dishDto);
        return R.success("修改菜品成功");
    }

    /**
     * 批量删除
     * @param ids
     * @return
     */
    @DeleteMapping
    public R<String> remove(String ids){
        dishService.removeByIds(ids);
        return R.success("删除成功");
    }


    //屎山代码，推翻重写

//    @PostMapping("/status/0")
//    public R<String> stopStatus (String ids){
//        dishService.stopStatus(0,ids);
//        return R.success("菜品停售修改成功");
//    }
//
//    @PostMapping("/status/1")
//    public R<String> startStatus (String ids){
//        dishService.stopStatus(0,ids);
//        return R.success("菜品停售修改成功");
//    }

    @PostMapping("/status/{status}")
    public R<String> stopStatus (@PathVariable int status,Long[] ids){
        dishService.editStatus(status,ids);
        return R.success("菜品状态修改成功");
    }

    /**
     * 获取菜品列表极其口味
     * @param categoryId
     * @return
     */
    @GetMapping("/list")
    public R<List<DishDto>> list(Dish categoryId){
        List<DishDto> dishDtoList = dishService.list(categoryId);
        return R.success(dishDtoList);
    }

}
