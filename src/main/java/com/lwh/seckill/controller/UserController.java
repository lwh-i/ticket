package com.lwh.seckill.controller;


import com.lwh.seckill.rabbitmq.MQSender;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author lwh
 * @since 2021-10-22
 */
@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    MQSender mqSender;

//    @RequestMapping("/mq")
//    @ResponseBody
//    public void mq(){
//        mqSender.send("hello");
//    }
}

