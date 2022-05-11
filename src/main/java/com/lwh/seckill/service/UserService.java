package com.lwh.seckill.service;

import com.lwh.seckill.entity.User;
import com.baomidou.mybatisplus.extension.service.IService;
import com.lwh.seckill.vo.LoginVo;
import com.lwh.seckill.vo.RegisterVo;
import com.lwh.seckill.vo.RespBean;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author lwh
 * @since 2021-10-22
 */
public interface UserService extends IService<User> {


    public  String getUuid();
    RespBean register(RegisterVo user);
    RespBean login(LoginVo loginVo, HttpServletRequest request, HttpServletResponse response);
    public User getUserByCookie(String userTicket,HttpServletRequest request,HttpServletResponse response) throws IOException;
    public RespBean updatePassword(String userTicket,String password,HttpServletRequest request, HttpServletResponse response) throws IOException;
}
