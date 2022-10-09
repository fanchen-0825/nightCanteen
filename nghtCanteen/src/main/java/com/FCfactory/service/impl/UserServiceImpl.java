package com.FCfactory.service.impl;

import com.FCfactory.entity.User;
import com.FCfactory.mapper.UserMapper;
import com.FCfactory.service.UserService;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpSession;
import java.util.Map;

@Service
@Slf4j
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {

    @Override
    public User longin(Map user, HttpSession session) {
        Object phone = user.get("phone");
        Object code = user.get("code");

        Object codeInSession = session.getAttribute("code");

        //查询是否存在改phone用户,不存在强行注册
        LambdaQueryWrapper<User> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(User::getPhone, phone);
        User one = this.getOne(wrapper);

        if(one==null){
             one = new User();
            one.setPhone(phone.toString());
            this.save(one);
        }

        //进行验证码的比对
        if(codeInSession!=null&&codeInSession.equals(code)){
            session.setAttribute("user", one.getId());
            return one;
        }
        return null;
    }
}
