package com.FCfactory.service;

import com.FCfactory.common.R;
import com.FCfactory.entity.Category;
import com.FCfactory.mapper.CategoryMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

public interface CategoryService extends IService<Category> {
    public Page<Category> getByPage (int page,int pageSize);

    public void remove(Long ids);

    public List<Category> selectList(Category category);
}
