package com.FCfactory.service;

import com.FCfactory.common.R;
import com.FCfactory.dto.DishDto;
import com.FCfactory.entity.Dish;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

public interface DishService extends IService<Dish> {

    public void addDishWithFlavor(DishDto dishDto);

    public Page<DishDto> getByPage(int page,int pageSize,String name);

    public DishDto getEditData(Long id);

    public void updateWithFlavor(DishDto dishDto);

    public void removeByIds(String ids);


    //屎山代码，推翻重写
//    public void stopStatus(int status,String ids);
//    public void startStatus(int status,String ids);

//    public void status(int status,String ids);

    public void editStatus(int status, Long[] ids);


    public List<DishDto> list(Dish categoryId);
}
