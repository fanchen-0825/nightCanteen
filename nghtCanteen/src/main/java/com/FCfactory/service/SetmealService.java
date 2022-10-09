package com.FCfactory.service;

import com.FCfactory.dto.DishDto;
import com.FCfactory.dto.SetmealDto;
import com.FCfactory.entity.Setmeal;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

public interface SetmealService extends IService<Setmeal> {
    public void saveSetmeal(SetmealDto setmealDto);

    public Page<SetmealDto> getByPage(int page,int pageSize,String name);

    public void editStatus(int status,Long[] ids);

    public void remove(Long[] ids);

    public List<Setmeal> SetmealList(Setmeal setmeal);
}
