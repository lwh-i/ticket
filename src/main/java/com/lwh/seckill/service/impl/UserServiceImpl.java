package com.lwh.seckill.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.lwh.seckill.entity.SeckillGoods;
import com.lwh.seckill.entity.User;
import com.lwh.seckill.exception.GlobalException;
import com.lwh.seckill.mapper.UserMapper;
import com.lwh.seckill.service.UserService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.lwh.seckill.utils.*;
import com.lwh.seckill.vo.LoginVo;
import com.lwh.seckill.vo.RegisterVo;
import com.lwh.seckill.vo.RespBean;
import com.lwh.seckill.vo.RespBeanEnum;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.thymeleaf.util.StringUtils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.CookieHandler;
import java.util.concurrent.TimeUnit;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author lwh
 * @since 2021-10-22
 */
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {


    @Autowired(required =false)
     private UserMapper userMapper;
  @Autowired
  private RedisTemplate redisTemplate;
  @Autowired
  private UserService userService;
    private  static  String user_id;



    @Override
    public RespBean login(LoginVo loginVo, HttpServletRequest request, HttpServletResponse response) {

        String  phone=loginVo.getPhone();
        String  password=loginVo.getPassword();
        User user=userMapper.selectOne(new QueryWrapper<User>().eq("phone",phone));

        if(user==null){
            return  RespBean.error(RespBeanEnum.PHONE_NULL );
        }
        if(!MD5Util.formPassToDB(password,user.getSlat()).equals(user.getPassword())){

            return  RespBean.error(RespBeanEnum.PASS_ERROR );
        }
//        生成cookie
        String ticket= UUIDUtil.uuid();
//        request.getSession().setAttribute(ticket,user);
//        将用户信息存储在redis中
        redisTemplate.opsForValue().set("user"+ticket, JsonUtil.obj2JsonStr(user));
        user_id="user"+ticket;
        System.out.println("user_uuid:"+user_id);
        CookieUtil.setCookie(request,response,"userTicket",ticket);
        return RespBean.success(ticket);
    }

    @Override
    public User getUserByCookie(String userTicket, HttpServletRequest request, HttpServletResponse response) throws IOException {
        if(StringUtils.isEmpty(userTicket)){
            return null;
        }
        String userJson=(String) redisTemplate.opsForValue().get("user"+userTicket);

        User user=JsonUtil.jsonStr2obj(userJson,User.class);
        if(user!=null){
            CookieUtil.setCookie(request,response,"userTicket",userTicket);
        }

        return user;
    }

    //    更新密码
    @Override
    public RespBean updatePassword(String userTicket,String password,HttpServletRequest request, HttpServletResponse response) throws IOException {
        User user = getUserByCookie(userTicket, request, response);
        if(user==null){
            throw  new GlobalException(RespBeanEnum.PHONE_NOEXIST);
        }
//        先更新数据库
        user.setPassword(MD5Util.inputToDBpass(password,user.getSlat()));
        int i = userMapper.updateById(user);
        if(1==i){
//            再删除密码
            redisTemplate.delete("user"+userTicket);
            return  RespBean.success();
        }
        return RespBean.error(RespBeanEnum.PASS_UPDATE_ERR);
    }

    @Override
    public String getUuid() {
        return user_id;
    }

    @Override
    public RespBean register(RegisterVo registerVo) {
        User user=new User();
        user.setNickname(registerVo.getUsername());
        user.setPhone(registerVo.getPhone());
        String dbpass=MD5Util.inputToDBpass(registerVo.getPassword(),"1234");
        user.setPassword(dbpass);
        boolean b = userService.save(user);
        User user1 = userMapper.selectById(user.getId());
        System.out.println("user="+user1);
        System.out.println("re="+b);
        if(b){
            return RespBean.success(RespBeanEnum.REGISTER_SUESS);
        }
        else{
            return RespBean.error(RespBeanEnum.REGISTER_ERROR);
        }

    }


}
