package com.lwh.seckill.controller;

import com.lwh.seckill.service.UserService;
import com.lwh.seckill.vo.RegisterVo;
import com.lwh.seckill.vo.RespBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;


@Controller
@RequestMapping("/register")
public class RegisterController {

    @Autowired
    UserService userService;
     @ResponseBody
    @RequestMapping(value = "/doRegister" ,method = RequestMethod.GET)
    public RespBean register(RegisterVo registerVo){
        return  userService.register(registerVo);
    }
}
