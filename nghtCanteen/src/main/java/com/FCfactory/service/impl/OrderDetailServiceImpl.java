package com.FCfactory.service.impl;

import com.FCfactory.entity.OrderDetail;
import com.FCfactory.mapper.OrderDetailMapper;
import com.FCfactory.service.OrderDetailService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class OrderDetailServiceImpl extends ServiceImpl<OrderDetailMapper, OrderDetail> implements OrderDetailService {
}
