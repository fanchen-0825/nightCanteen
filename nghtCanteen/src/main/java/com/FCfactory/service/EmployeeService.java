package com.FCfactory.service;

import com.FCfactory.common.R;
import com.FCfactory.entity.Employee;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;

public interface EmployeeService extends IService<Employee> {
    public Page<Employee> getByPage(int page,int pageSize,String name);
}
