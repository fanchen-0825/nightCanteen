package com.FCfactory.controller;

import com.FCfactory.common.R;
import com.FCfactory.entity.Category;
import com.FCfactory.entity.ShoppingCart;
import com.FCfactory.service.CategoryService;
import com.FCfactory.service.ShoppingCartService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/shoppingCart")
@Slf4j
public class ShoppingCartController {

    @Autowired
    private ShoppingCartService shoppingCartService;

    @Autowired
    private CategoryService categoryService;

    /**
     * 添加菜品或套餐
     * @param shoppingCart
     * @return
     */
    @PostMapping("/add")
    public R<ShoppingCart> add(@RequestBody ShoppingCart shoppingCart){
        log.info(shoppingCart.toString());
        ShoppingCart cart = shoppingCartService.add(shoppingCart);
        return R.success(cart);

    }

    /**
     * 获取购物车信息
     * @return
     */
    @GetMapping("/list")
    public R<List<ShoppingCart>> list(){
        List<ShoppingCart> list = shoppingCartService.getList();
        return R.success(list);
    }


    /**
     * 减少菜品数量
     * @param shoppingCart
     * @return
     */
    @PostMapping("/sub")
    public R<ShoppingCart> sub(@RequestBody ShoppingCart shoppingCart){
        log.info(shoppingCart.toString());
        ShoppingCart cart = shoppingCartService.sub(shoppingCart);
        return R.success(cart);
    }

    @DeleteMapping("/clean")
    public R<String> deleteAll(){
        shoppingCartService.deleteAll();
        return R.success("清空购物车成功");
    }
}
