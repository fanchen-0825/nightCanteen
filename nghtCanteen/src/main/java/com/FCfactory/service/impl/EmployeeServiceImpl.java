package com.FCfactory.service.impl;

import com.FCfactory.common.R;
import com.FCfactory.entity.Employee;
import com.FCfactory.mapper.EmployeeMapper;
import com.FCfactory.service.EmployeeService;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.apache.logging.log4j.util.Strings;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class EmployeeServiceImpl extends ServiceImpl<EmployeeMapper, Employee> implements EmployeeService {

    @Autowired
    private EmployeeMapper employeeMapper;

    @Override
    public Page<Employee> getByPage(int page, int pageSize, String name) {
        Page<Employee> employeePage = new Page<Employee>(page, pageSize);
        LambdaQueryWrapper<Employee> wrapper = new LambdaQueryWrapper<Employee>();
        wrapper.like(Strings.isNotEmpty(name),Employee::getName, name);
        wrapper.orderByDesc(Employee::getUpdateTime);
        employeeMapper.selectPage(employeePage,wrapper);
        return employeePage;
    }
}
