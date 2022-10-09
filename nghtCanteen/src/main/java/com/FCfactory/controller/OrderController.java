package com.FCfactory.controller;

import com.FCfactory.common.R;
import com.FCfactory.entity.Orders;
import com.FCfactory.entity.ShoppingCart;
import com.FCfactory.service.OrderService;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/order")
@Slf4j
public class OrderController {
    @Autowired
    private OrderService orderService;

    @PostMapping("/submit")
    public R<String> submit(@RequestBody Orders orders){
        log.info("订单数据：{}",orders.toString());
        orderService.submit(orders);
        return R.success("下单成功");
    }


    @GetMapping("/page")
    public R<Page> getByPage(int page,int pageSize){
        Page<Orders> ordersPage = orderService.getByPage(page, pageSize);
        return R.success(ordersPage);
    }
}
