package com.FCfactory.service.impl;

import com.FCfactory.common.CustomException;
import com.FCfactory.common.R;
import com.FCfactory.entity.Category;
import com.FCfactory.entity.Dish;
import com.FCfactory.entity.Setmeal;
import com.FCfactory.mapper.CategoryMapper;
import com.FCfactory.mapper.DishMapper;
import com.FCfactory.mapper.SetmealMapper;
import com.FCfactory.service.CategoryService;
import com.FCfactory.service.DishService;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.DeleteMapping;

import java.util.List;

@Service
public class CategoryServiceImpl extends ServiceImpl<CategoryMapper, Category> implements CategoryService {

    @Autowired
    private CategoryMapper categoryMapper;

    @Autowired
    private DishMapper dishMapper;

    @Autowired
    private SetmealMapper setmealMapper;

    /**
     * 分页查询
     *
     * @param page
     * @param pageSize
     * @return
     */
    @Override
    public Page<Category> getByPage(int page, int pageSize) {
        Page<Category> categoryPage = new Page<Category>(page, pageSize);
        LambdaQueryWrapper<Category> wrapper = new LambdaQueryWrapper<>();
        wrapper.orderByAsc(Category::getSort);
        categoryMapper.selectPage(categoryPage, wrapper);
        return categoryPage;
    }

    /**
     * 删除 删除前查询是否存在关联
     *
     * @param ids
     */
    @Override
    public void remove(Long ids) {
        LambdaQueryWrapper<Dish> dishLambdaQueryWrapper = new LambdaQueryWrapper<>();
        dishLambdaQueryWrapper.eq(Dish::getCategoryId, ids);
        Integer count1 = dishMapper.selectCount(dishLambdaQueryWrapper);
        if (count1 > 0) {
            throw new CustomException("当前分类下关联了菜品");
        }

        LambdaQueryWrapper<Setmeal> setmealLambdaQueryWrapper = new LambdaQueryWrapper<>();
        setmealLambdaQueryWrapper.eq(Setmeal::getCategoryId, ids);
        Integer count2 = setmealMapper.selectCount(setmealLambdaQueryWrapper);
        if (count2 > 0) {
            throw new CustomException("当前套餐下关联了菜品");
        }
        categoryMapper.deleteById(ids);
    }

    /**
     * 查询菜品列表
     * @param category
     * @return
     */
    @Override
    public List<Category> selectList(Category category){
        LambdaQueryWrapper<Category> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(category.getType()!=null,Category::getType,category.getType());
        wrapper.orderByAsc(Category::getSort).orderByDesc(Category::getUpdateTime);
        List<Category> categories = categoryMapper.selectList(wrapper);
        return categories;
    }
}
