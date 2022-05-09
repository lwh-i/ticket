package com.lwh.seckill.controller;


import com.lwh.seckill.entity.User;
import com.lwh.seckill.service.IOrderService;
import com.lwh.seckill.service.UserService;
import com.lwh.seckill.utils.JsonUtil;
import com.lwh.seckill.vo.OrderDetailVo;
import com.lwh.seckill.vo.RespBean;
import com.lwh.seckill.vo.RespBeanEnum;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author lwh
 * @since 2021-11-04
 */
@Controller
@RequestMapping("/order")
public class OrderController {


    @Autowired
    private IOrderService orderService;
    @Autowired
    private UserService userService;
    @Autowired
    private RedisTemplate redisTemplate;


    @RequestMapping(value = "/detail",method = RequestMethod.GET)
    @ResponseBody
    public RespBean detail(User user, Long orderId) throws IOException {
        //         参数解析器不起作用在云服务器上 可能是cookie的原因 也可能是其他原因
        String uuid=userService.getUuid();
        user = JsonUtil.jsonStr2obj(redisTemplate.opsForValue().get(uuid).toString(),User.class);
        if(null==user){
            return RespBean.error(RespBeanEnum.SESSION_TIMEOUT);
        }
        OrderDetailVo detailVo=orderService.detail(orderId,user);
        return RespBean.success(detailVo);
    }

}
