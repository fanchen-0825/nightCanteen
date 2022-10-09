package com.FCfactory.service.impl;

import com.FCfactory.common.R;
import com.FCfactory.dto.DishDto;
import com.FCfactory.entity.Category;
import com.FCfactory.entity.Dish;
import com.FCfactory.entity.DishFlavor;
import com.FCfactory.mapper.DishMapper;
import com.FCfactory.service.CategoryService;
import com.FCfactory.service.DishFlavorService;
import com.FCfactory.service.DishService;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.apache.logging.log4j.util.Strings;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@Slf4j
public class DishServiceImpl extends ServiceImpl<DishMapper, Dish> implements DishService {
    @Autowired
    private DishFlavorService dishFlavorService;

    @Autowired
    private CategoryService categoryService;

    @Autowired
    private DishMapper dishMapper;

    /**
     * 添加菜品 操作两张表
     *
     * @param dishDto
     */
    @Override
    @Transactional
    public void addDishWithFlavor(DishDto dishDto) {
        this.save(dishDto);

        Long dishId = dishDto.getId();
        List<DishFlavor> flavors = dishDto.getFlavors();
        for (DishFlavor flavor : flavors) {
            flavor.setDishId(dishId);
        }
        dishFlavorService.saveBatch(flavors);
    }


    /**
     * 菜品分页查询
     *
     * @param page
     * @param pageSize
     * @param name
     * @return
     */
    @Override
    public Page<DishDto> getByPage(int page, int pageSize, String name) {
        Page<Dish> dishPage = new Page<>(page, pageSize);
        Page<DishDto> dishDtoPage = new Page<>();

        LambdaQueryWrapper<Dish> wrapper = new LambdaQueryWrapper<>();
        wrapper.like(name != null, Dish::getName, name);
        wrapper.orderByDesc(Dish::getUpdateTime);
        this.page(dishPage, wrapper);


        BeanUtils.copyProperties(dishPage, dishDtoPage, "records");
        List<Dish> dishRecords = dishPage.getRecords();
        List<DishDto> dishDtoList = new ArrayList<>();
        for (Dish dishRecord : dishRecords) {
            DishDto dishDto = new DishDto();
            BeanUtils.copyProperties(dishRecord, dishDto);
            Long categoryId = dishRecord.getCategoryId();
            Category category = categoryService.getById(categoryId);
            if (category != null) {
                String categoryName = category.getName();
                dishDto.setCategoryName(categoryName);
            }
            dishDtoList.add(dishDto);
        }
        dishDtoPage.setRecords(dishDtoList);
        return dishDtoPage;
    }

    /**
     * 获取编辑页面回显的数据
     *
     * @param id
     * @return
     */
    @Override
    public DishDto getEditData(Long id) {
        Dish dish = this.getById(id);
        LambdaQueryWrapper<DishFlavor> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(DishFlavor::getDishId, dish.getId());
        List<DishFlavor> flavorList = dishFlavorService.list(wrapper);
        DishDto dishDto = new DishDto();
        BeanUtils.copyProperties(dish, dishDto);
        dishDto.setFlavors(flavorList);
        return dishDto;
    }

    @Override
    @Transactional
    public void updateWithFlavor(DishDto dishDto) {
        this.updateById(dishDto);
        Long dishId = dishDto.getId();
        LambdaQueryWrapper<DishFlavor> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(DishFlavor::getDishId, dishId);
        dishFlavorService.remove(wrapper);

        List<DishFlavor> flavors = dishDto.getFlavors();
        for (DishFlavor flavor : flavors) {
            flavor.setDishId(dishId);
        }

        dishFlavorService.saveBatch(flavors);
    }

    @Override
    public void removeByIds(String ids) {
        String[] strings = ids.split(",");
        List<Long> idList = new ArrayList<>();

        for (String string : strings) {
            long aLong = Long.parseLong(string);
            idList.add(aLong);
        }
        this.removeByIds(idList);
    }


    //屎山代码，推翻重写

//    @Override
//    public void stopStatus(int status,String ids) {
//        String[] strings = ids.split(",");
//        List<Long> idList=new ArrayList<>();
//
//        for (String string : strings) {
//            long aLong = Long.parseLong(string);
//            idList.add(aLong);
//        }
//        for (Long aLong : idList) {
//            dishMapper.editStatus(status,aLong);
//        }
//
//    }
//
//    @Override
//    public void startStatus(int status,String ids) {
//        String[] strings = ids.split(",");
//        List<Long> idList=new ArrayList<>();
//
//        for (String string : strings) {
//            long aLong = Long.parseLong(string);
//            idList.add(aLong);
//        }
//        for (Long aLong : idList) {
//            dishMapper.editStatus(status,aLong);
//        }
//    }

//    @Override
//    public void status(int status, String ids) {
//        String[] strings = ids.split(",");
//        List<Long> idList = new ArrayList<>();
//
//        for (String string : strings) {
//            long aLong = Long.parseLong(string);
//            idList.add(aLong);
//        }
//        for (Long aLong : idList) {
//            dishMapper.editStatus(status, aLong);
//        }
//    }


    /**
     * 修改状态（停售 起售）
     * @param status
     * @param ids
     */
    @Override
    public void editStatus(int status, Long[] ids) {

        LambdaQueryWrapper<Dish> wrapper = new LambdaQueryWrapper<>();
        wrapper.in(Dish::getId,ids);
        Dish dish = new Dish();
        dish.setStatus(status);
        this.update(dish,wrapper);
    }

//    @Override
//    public List<Dish> list(Dish categoryId) {
//        LambdaQueryWrapper<Dish> wrapper = new LambdaQueryWrapper<>();
//        wrapper.eq(categoryId.getCategoryId()!=null,Dish::getCategoryId,categoryId.getCategoryId())
//                .eq(Dish::getStatus,1);
//        wrapper.orderByAsc(Dish::getSort).orderByDesc(Dish::getUpdateTime);
//        List<Dish> dishList = this.list(wrapper);
//        return dishList;
//    }

    @Override
    public List<DishDto> list(Dish categoryId){
        LambdaQueryWrapper<Dish> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(categoryId.getCategoryId()!=null,Dish::getCategoryId,categoryId.getCategoryId());
        wrapper.eq(Dish::getStatus, 1);
        List<Dish> dishList = this.list(wrapper);
        List<DishDto> dishDtoList=new ArrayList<>();
        for (Dish dish : dishList) {
            Long dishId = dish.getId();
            LambdaQueryWrapper<DishFlavor> flavorWrapper = new LambdaQueryWrapper<>();
            flavorWrapper.eq(DishFlavor::getDishId,dishId);
            List<DishFlavor> dishFlavors = dishFlavorService.list(flavorWrapper);
            DishDto dishDto = new DishDto();
            BeanUtils.copyProperties(dish,dishDto);
            dishDto.setFlavors(dishFlavors);
            dishDtoList.add(dishDto);
        }
        return dishDtoList;

    }
}
