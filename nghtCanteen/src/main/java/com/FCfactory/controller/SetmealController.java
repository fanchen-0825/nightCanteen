package com.FCfactory.controller;

import com.FCfactory.common.R;
import com.FCfactory.dto.SetmealDto;
import com.FCfactory.entity.Setmeal;
import com.FCfactory.service.SetmealDishService;
import com.FCfactory.service.SetmealService;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.annotations.Delete;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/setmeal")
@Slf4j
public class SetmealController {
    @Autowired
    private SetmealDishService setmealDishService;

    @Autowired
    private SetmealService setmealService;


    /**
     * 新增套餐
     * @param setmealDto
     * @return
     */
    @PostMapping
    public R<String> save(@RequestBody SetmealDto setmealDto){
        log.info(setmealDto.toString());
        setmealService.saveSetmeal(setmealDto);
        return R.success("新增套餐成功");
    }

    /**
     * 套餐分页查询
     * @param page
     * @param pageSize
     * @param name
     * @return
     */
    @GetMapping("/page")
    public R<Page<SetmealDto>> getByPage(int page,int pageSize,String name){
        Page<SetmealDto> dtoPage = setmealService.getByPage(page, pageSize, name);
        return R.success(dtoPage);
    }

    /**
     * 状态修改
     * @param status
     * @param ids
     * @return
     */
    @PostMapping("/status/{status}")
    public R<String> editStatus(@PathVariable int status,Long[] ids){
        setmealService.editStatus(status,ids);
        return R.success("修改套餐状态成功");
    }

    /**
     * 删除套餐
     * @param ids
     * @return
     */
    @DeleteMapping
    public R<String> remove(Long[] ids){
        setmealService.remove(ids);
        return R.success("套餐删除成功");
    }

    /**
     * 根据条件查询套餐
     * @param setmeal
     * @return
     */
    @GetMapping("/list")
    public R<List<Setmeal>> list(Setmeal setmeal){
        List<Setmeal> setmealList = setmealService.SetmealList(setmeal);
        return R.success(setmealList);
    }
}
