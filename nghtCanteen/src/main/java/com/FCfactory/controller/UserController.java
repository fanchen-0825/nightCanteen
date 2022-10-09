package com.FCfactory.controller;

import com.FCfactory.common.R;
import com.FCfactory.entity.User;
import com.FCfactory.service.UserService;
import com.FCfactory.utils.SendSmsUtils;
import com.FCfactory.utils.ValidateCodeUtils;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import lombok.extern.slf4j.Slf4j;
import org.apache.logging.log4j.util.Strings;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpSession;
import java.util.Map;

@RestController
@RequestMapping("/user")
@Slf4j
public class UserController {
    @Autowired
    private UserService userService;

    @PostMapping("/sendMsg")
    public R<String> sengMsg(@RequestBody User user, HttpSession session){
        String phone = user.getPhone();
        log.info("手机号：{}",phone);

        phone="+86"+phone;
        log.info("处理后手机号：{}",phone);

        if(Strings.isNotEmpty(phone)){
            //生成随机验证码
            String code = ValidateCodeUtils.generateValidateCode(4).toString();
            log.info("code:{}",code);
            //调用短信服务（腾讯云）
            //此行代码普通测试时不要放开，因为扣费！！！
            SendSmsUtils.sendMessage(phone,code);

            session.setAttribute("phone",phone);
            session.setAttribute("code",code);

            return R.success("短信发送成功");

        }
        return R.error("短信发送失败");
    }

    @PostMapping("/login")
    public R<User> login(@RequestBody Map user,HttpSession session){
        log.info(user.toString());
        User user1 = userService.longin(user, session);
        if (user!=null){
            return R.success(user1);
        }
        return R.error("验证码错误");
    }

    @PostMapping("/loginout")
    public R<String> loginout(HttpSession httpSession){
        log.info(httpSession.getAttribute("user").toString());
        httpSession.removeAttribute("user");
        httpSession.removeAttribute("code");
        //log.info(httpSession.getAttribute("user").toString());
        return R.success("退出成功");
    }
}
