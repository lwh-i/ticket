package com.lwh.seckill.vo;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.apache.logging.log4j.message.Message;

//公共返回对象枚举
@ToString
@AllArgsConstructor
@Getter

public enum RespBeanEnum {
//    通用
    SUCCESS(200,"SUCCESS"),
    ERROR(500,"服务器异常11"),
//    登录模块5002**
    LOGIN_ERROR(500210,"用户名或密码为空"),
    PHONE_NULL(500213,"账号不存在"),
    PASS_ERROR(500214,"密码错误"),
    PHONE_ERROR(500211,"手机号码格式错误"),
    ARG_ERROR(500212,"参数校验异常"),
    PHONE_NOEXIST(500215,"手机号不存在"),
    PASS_UPDATE_ERR(500216,"更新密码失败"),
    LOGIN_TIMEOUT(500217,"登录失效请重新登录"),
    SESSION_TIMEOUT(500218,"用户失效请重新登录"),
    REQUEST_FAIL(500219,"请求非法，请重试"),
//注册模块
    REGISTER_SUESS(500600,"注册成功"),
    REGISTER_ERROR(500601,"注册失败"),
//    秒杀模块5005**

    EMPTY_STOCK(500500,"库存不足"),
    ORDER_NOTEXIST(500502,"订单不存在"),
    REBUY_ERROR(500501,"该商品限购一件"),
    ERROR_CAPTCHA(500504,"验证码错误"),
    REQUEST_MULT(500503,"请求过于频繁请稍后再试");//三秒10次
    private  final Integer code;
    private final String message;
}
