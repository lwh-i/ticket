package com.lwh.seckill.controller;


import com.lwh.seckill.service.UserService;
import com.lwh.seckill.service.impl.UserServiceImpl;
import com.lwh.seckill.vo.LoginVo;
import com.lwh.seckill.vo.RespBean;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

@Controller   //页面跳转返回一个页面      restController默认加上一个responendsBody返回一个对象
@RequestMapping("/login")
@Slf4j
public class LoginController {

    @Autowired
    private UserService userService;
    @RequestMapping("/toLogin")
    public String toLogin() {
        return "login";
    }
    @RequestMapping("/doLogin")
    @ResponseBody
    public RespBean doLogin(@Valid  LoginVo loginVo, HttpServletRequest request, HttpServletResponse response){
        log.info("loginVo={}",loginVo);
        return userService.login(loginVo,request,response);
    }

}
