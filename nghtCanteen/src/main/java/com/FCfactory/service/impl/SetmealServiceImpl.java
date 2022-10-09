package com.FCfactory.service.impl;

import com.FCfactory.common.CustomException;
import com.FCfactory.dto.DishDto;
import com.FCfactory.dto.SetmealDto;
import com.FCfactory.entity.Category;
import com.FCfactory.entity.Setmeal;
import com.FCfactory.entity.SetmealDish;
import com.FCfactory.mapper.SetmealMapper;
import com.FCfactory.service.CategoryService;
import com.FCfactory.service.SetmealDishService;
import com.FCfactory.service.SetmealService;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Service
@Slf4j
public class SetmealServiceImpl extends ServiceImpl<SetmealMapper, Setmeal> implements SetmealService {

    @Autowired
    private SetmealDishService setmealDishService;

    @Autowired
    private CategoryService categoryService;

    /**
     * 新增操作 同时操作两张表
     * @param setmealDto
     */
    @Override
    @Transactional
    public void saveSetmeal(SetmealDto setmealDto) {
        this.save(setmealDto);

        List<SetmealDish> dishList = setmealDto.getSetmealDishes();
        for (SetmealDish dish : dishList) {
            log.info(dish.toString());
            dish.setSetmealId(setmealDto.getId());
        }
        setmealDishService.saveBatch(dishList);
    }


    /**
     * 套餐分页查询
     * @param page
     * @param pageSize
     * @param name
     * @return
     */
    @Override
    public Page<SetmealDto> getByPage(int page, int pageSize, String name) {
        Page<Setmeal> setmealPage = new Page<>(page,pageSize);
        Page<SetmealDto> setmealDtoPage = new Page<>();

        LambdaQueryWrapper<Setmeal> wrapper = new LambdaQueryWrapper<>();
        wrapper.like(name!=null,Setmeal::getName, name);
        wrapper.orderByDesc(Setmeal::getUpdateTime);
        this.page(setmealPage,wrapper);

        BeanUtils.copyProperties(setmealPage,setmealDtoPage,"records");

        List<Setmeal> records = setmealPage.getRecords();
        List<SetmealDto> setmealDtoList=new ArrayList<>();
        for (Setmeal record : records) {
            Long categoryId = record.getCategoryId();
            Category category = categoryService.getById(categoryId);
            String categoryName = category.getName();

            SetmealDto setmealDto = new SetmealDto();
            BeanUtils.copyProperties(record,setmealDto);
            setmealDto.setCategoryName(categoryName);
            setmealDtoList.add(setmealDto);
        }
        setmealDtoPage.setRecords(setmealDtoList);

        return setmealDtoPage;
    }

    /**
     * 修改状态
     * @param status
     * @param ids
     */
    @Override
    public void editStatus(int status, Long[] ids) {
        LambdaQueryWrapper<Setmeal> wrapper = new LambdaQueryWrapper<>();
        wrapper.in(Setmeal::getId,ids);
        Setmeal setmeal = new Setmeal();
        setmeal.setStatus(status);
        this.update(setmeal,wrapper);
    }

    /**
     * 删除套餐
     * @param ids
     */
    @Override
    public void remove(Long[] ids) {
        LambdaQueryWrapper<Setmeal> wrapper = new LambdaQueryWrapper<>();
        wrapper.in(Setmeal::getId,ids);
        wrapper.eq(Setmeal::getStatus,1);
        int count = this.count(wrapper);
        if(count>0){
            throw new CustomException("所选套餐在售，不可删除");
        }

        this.removeByIds(Arrays.asList(ids));

        LambdaQueryWrapper<SetmealDish> dishWrapper = new LambdaQueryWrapper<>();
        dishWrapper.in(SetmealDish::getSetmealId,ids);
        setmealDishService.remove(dishWrapper);
    }

    @Override
    public List<Setmeal> SetmealList(Setmeal setmeal) {
        LambdaQueryWrapper<Setmeal> wrapper = new LambdaQueryWrapper<>();
        Long categoryId = setmeal.getCategoryId();
        wrapper.eq(categoryId!=null,Setmeal::getCategoryId, categoryId);
        wrapper.eq(setmeal.getStatus()!=null,Setmeal::getStatus,setmeal.getStatus());
        wrapper.orderByDesc(Setmeal::getUpdateTime);
        List<Setmeal> setmealList = this.list(wrapper);

        return setmealList;
    }
}
