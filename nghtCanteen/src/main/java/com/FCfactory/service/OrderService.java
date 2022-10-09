package com.FCfactory.service;

import com.FCfactory.common.R;
import com.FCfactory.entity.Orders;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import org.springframework.core.annotation.Order;

public interface OrderService extends IService<Orders> {
    public void submit(Orders orders);

    public Page<Orders> getByPage(int page, int pageSize);
}
