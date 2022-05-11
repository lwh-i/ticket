package com.lwh.seckill.controller;

import com.lwh.seckill.entity.User;
import com.lwh.seckill.service.UserService;
import com.lwh.seckill.vo.RegisterVo;
import com.lwh.seckill.vo.RespBean;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.validation.Valid;


@Controller
@Slf4j
@RequestMapping(value = "/register")
public class RegisterController {

    @Autowired
    UserService userService;
    @ResponseBody
    @RequestMapping(value = "/doRegister" )
    public RespBean register( RegisterVo user){
         log.info("user={}",user);
        return  userService.register(user);
    }
}
