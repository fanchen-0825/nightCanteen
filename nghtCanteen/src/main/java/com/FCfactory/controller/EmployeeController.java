package com.FCfactory.controller;

import com.FCfactory.common.MyMetaObjectHandler;
import com.FCfactory.common.R;
import com.FCfactory.entity.Employee;
import com.FCfactory.service.EmployeeService;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import jdk.nashorn.internal.objects.Global;
import lombok.extern.slf4j.Slf4j;
import org.apache.logging.log4j.util.Strings;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpRequest;
import org.springframework.stereotype.Controller;
import org.springframework.util.DigestUtils;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.websocket.server.PathParam;
import java.time.LocalDateTime;

@Slf4j
@RestController
@RequestMapping("/employee")

public class EmployeeController {
    @Autowired
    private EmployeeService employeeService;

    /**
     * 登录
     *
     * @param request  将登录成功用户存到Session中
     * @param employee 前端发送的请求中的Employee对象
     * @return
     */
    @PostMapping("/login")
    public R<Employee> login(HttpServletRequest request, @RequestBody Employee employee) {
        log.info(employee.getUsername());
        log.info(employee.getPassword());
        String password = employee.getPassword();
        //password=DigestUtils.md5Digest(password.getBytes()).toString();        e10adc3949ba59abbe56e057f20f883e
        password = DigestUtils.md5DigestAsHex(password.getBytes());
        LambdaQueryWrapper<Employee> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Employee::getUsername, employee.getUsername());
        //根据条件进行唯一id查询
        Employee emp = employeeService.getOne(wrapper);

        if (emp == null) {
            return R.error("登陆失败");
        }

        if (!emp.getPassword().equals(password)) {
            return R.error("登录失败");
        }

        if (emp.getStatus() == 0) {
            return R.error("账号已禁用");
        }

        request.getSession().setAttribute("employee", emp.getId());
        return R.success(emp);
    }


    /**
     * 退出
     *
     * @param request
     * @return
     */
    @PostMapping("/logout")
    public R<String> logout(HttpServletRequest request) {

        //清理session数据
        request.removeAttribute("employee");
        return R.success("退出成功");
    }

    /**
     * 添加操作
     *
     * @param request
     * @param employee
     * @return
     */
    @PostMapping
    public R<String> save(HttpServletRequest request, @RequestBody Employee employee) {
        log.info("新增员工信息：{}", employee.toString());
        employee.setPassword(DigestUtils.md5DigestAsHex("123456".getBytes()));
//        employee.setCreateTime(LocalDateTime.now());
//        employee.setUpdateTime(LocalDateTime.now());
//        Long id = (Long) request.getSession().getAttribute("employee");
//        employee.setCreateUser(id);
//        employee.setUpdateUser(id);


        boolean save = employeeService.save(employee);
        System.out.println(save);
        return R.success("添加成功");
    }


    /**
     * 分页查询
     *
     * @param page     当前页
     * @param pageSize 每页显示页码数
     * @param name     查询条件
     * @return
     */
    @GetMapping("/page")
    public R<Page> getByPage(int page, int pageSize, String name) {
        log.info("page={} pageSize={} name={}", page, pageSize, name);
        Page<Employee> byPage = employeeService.getByPage(page, pageSize, name);
        return R.success(byPage);
    }

    /**
     * 修改   可进行状态修改和编辑信息
     *
     * @param request
     * @param employee
     * @return
     */
    @PutMapping
    public R<String> update(HttpServletRequest request, @RequestBody Employee employee) {
//        employee.setUpdateTime(LocalDateTime.now());
//        Long id = (Long) request.getSession().getAttribute("employee");
//        employee.setUpdateUser(id);
        boolean b = employeeService.updateById(employee);
        if (!b) {
            return R.success("请求出错！");
        }
        return R.success("修改成功");
    }


    @GetMapping("/{id}")
    public R<Employee> getById(@PathVariable Long id) {
        Employee employee = employeeService.getById(id);
        return R.success(employee);
    }
}
