package com.lwh.seckill.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/demo")
public class DemoController {
//    测试页面跳转
    @RequestMapping("/hello")
    public String hello(Model model){
        model.addAttribute("lwh","hello");
        return "hello";
    }
}
