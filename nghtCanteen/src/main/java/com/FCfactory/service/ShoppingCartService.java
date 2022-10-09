package com.FCfactory.service;

import com.FCfactory.entity.ShoppingCart;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

public interface ShoppingCartService extends IService<ShoppingCart> {
    public ShoppingCart add(ShoppingCart shoppingCart);

    public List<ShoppingCart> getList();

    public ShoppingCart sub(ShoppingCart shoppingCart);

    public void deleteAll();
}
