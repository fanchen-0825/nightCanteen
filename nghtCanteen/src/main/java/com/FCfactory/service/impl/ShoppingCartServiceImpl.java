package com.FCfactory.service.impl;

import com.FCfactory.common.BaseContext;
import com.FCfactory.entity.ShoppingCart;
import com.FCfactory.mapper.ShoppingCartMapper;
import com.FCfactory.service.ShoppingCartService;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
public class ShoppingCartServiceImpl extends ServiceImpl<ShoppingCartMapper, ShoppingCart> implements ShoppingCartService {

    @Override
    public ShoppingCart add(ShoppingCart shoppingCart) {
        //设置用户id
        Long currentId = BaseContext.getCurrentId();
        //获取前台发送的菜品或套餐id
        Long dishId = shoppingCart.getDishId();

        LambdaQueryWrapper<ShoppingCart> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(ShoppingCart::getUserId, currentId);
        //构造查询条件
        if (dishId != null) {
            //是菜品 添加菜品的查询条件
            wrapper.eq(ShoppingCart::getDishId, dishId);

        } else {
            //是套餐 添加套餐的查询条件
            wrapper.eq(ShoppingCart::getSetmealId, shoppingCart.getSetmealId());
        }
        //判断该菜品或套餐是否已存在 若存在number+1 不存在则新增
        ShoppingCart cart = this.getOne(wrapper);

        if (cart != null) {
            Integer number = cart.getNumber();
            number++;
            cart.setNumber(number);
            this.updateById(cart);
        } else {
            shoppingCart.setUserId(currentId);
            this.save(shoppingCart);
            cart = shoppingCart;
        }
        return cart;
    }

    @Override
    public List<ShoppingCart> getList() {
        LambdaQueryWrapper<ShoppingCart> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(ShoppingCart::getUserId,BaseContext.getCurrentId());
        wrapper.orderByAsc(ShoppingCart::getCreateTime);
        List<ShoppingCart> shoppingCartList = this.list(wrapper);
        return shoppingCartList;
    }

    @Override
    public ShoppingCart sub(ShoppingCart shoppingCart) {
        //获取当前id
        Long currentId = BaseContext.getCurrentId();
        Long dishId = shoppingCart.getDishId();

        //构造查询条件
        LambdaQueryWrapper<ShoppingCart> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(ShoppingCart::getUserId,currentId);
        //判断是菜品还是套餐
        if(dishId!=null){
            //添加菜品的修改条件
            wrapper.eq(ShoppingCart::getDishId,dishId);
        }else{
            //添加套餐的修改条件
            wrapper.eq(ShoppingCart::getSetmealId,shoppingCart.getSetmealId());
        }

        ShoppingCart cart = this.getOne(wrapper);
        Integer number = cart.getNumber();
        if(number>0){
            number--;
            if(number==0){
                Long id = cart.getId();
                this.removeById(id);
                return new ShoppingCart();
            } else {
                cart.setNumber(number);
                this.updateById(cart);
            }
        }
        return cart;
    }

    @Override
    public void deleteAll(){
        LambdaQueryWrapper<ShoppingCart> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(ShoppingCart::getUserId,BaseContext.getCurrentId());
        this.remove(wrapper);
    }
}
