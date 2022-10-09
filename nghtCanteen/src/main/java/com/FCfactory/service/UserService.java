package com.FCfactory.service;

import com.FCfactory.entity.User;
import com.baomidou.mybatisplus.extension.service.IService;

import javax.servlet.http.HttpSession;
import java.util.Map;

public interface UserService extends IService<User> {
    public User longin(Map user, HttpSession session);
}
