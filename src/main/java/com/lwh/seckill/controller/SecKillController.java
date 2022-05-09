package com.lwh.seckill.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.lwh.seckill.config.AccessLimit;
import com.lwh.seckill.entity.Order;
import com.lwh.seckill.entity.SeckillMessage;
import com.lwh.seckill.entity.SeckillOrder;
import com.lwh.seckill.entity.User;
import com.lwh.seckill.exception.GlobalException;
import com.lwh.seckill.rabbitmq.MQSender;
import com.lwh.seckill.service.IGoodsService;
import com.lwh.seckill.service.IOrderService;
import com.lwh.seckill.service.ISeckillOrderService;
import com.lwh.seckill.service.UserService;
import com.lwh.seckill.utils.CookieUtil;
import com.lwh.seckill.utils.JsonUtil;
import com.lwh.seckill.vo.GoodsVo;
import com.lwh.seckill.vo.RespBean;
import com.lwh.seckill.vo.RespBeanEnum;
import com.wf.captcha.ArithmeticCaptcha;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.*;
import org.thymeleaf.util.StringUtils;

import javax.lang.model.element.VariableElement;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/*秒杀*/
@Controller
@Slf4j
@RequestMapping("/secKill")
public class SecKillController implements InitializingBean {
    @Autowired
    IGoodsService goodsService;
    @Autowired
    ISeckillOrderService seckillOrderService;
    @Autowired
    IOrderService orderService;
    @Autowired
    RedisTemplate redisTemplate;
    @Autowired
    UserService userService;
    @Autowired
    MQSender mqSender;


    private Map<Long,Boolean> EmptyStockMap=new HashMap<>();
    @RequestMapping("/doSecKill2")
    public String doSecKill2(Model model, User user, Long goodsId) throws IOException {

//        判断用户是否登录   user是否为空
        String uuid = userService.getUuid();
        if (uuid == null) {
            return "login";
        }
        Boolean aBoolean = redisTemplate.hasKey(uuid);
        if (!aBoolean) {
            return "login";
        }
        String str = redisTemplate.opsForValue().get(uuid) + "";
        user = JsonUtil.jsonStr2obj(str, User.class);

        System.out.println("user=" + user);
//        bug1 ：user一直为空
        if (user == null) {
            return "login";
        }

        System.out.println("user=" + user);
        System.out.println("jinlaile");
        model.addAttribute("user", user);
        GoodsVo goodsVo = goodsService.findGoodsVoByGoodsId(goodsId);
        System.out.println("goodsVo"+goodsVo.getStockCount());
//        判断商品库存 判断秒杀商品对象的库存是否大于0eq
        if (goodsVo.getStockCount() <= 0) {
            model.addAttribute("errmsg", RespBeanEnum.EMPTY_STOCK.getMessage());
            return "secKillFail";
        }
//        判断订单是否一个商品被重复购买   根据秒杀订单表里面的用户id和商品id查询订单 能查到就是这个用户已经买过了该商品,返回一个Err信息
        SeckillOrder seckillOrder = seckillOrderService.getOne(new QueryWrapper<SeckillOrder>().eq("user_id", user.getId()).
                eq("goods_id", goodsId));
        if (seckillOrder != null) {
            model.addAttribute("errmsg", RespBeanEnum.REBUY_ERROR.getMessage());
            return "secKillFail";
        }
        Order order = orderService.seckill(user, goodsVo);
        model.addAttribute("order", order);
        model.addAttribute("goods", goodsVo);
        return "orderDetail";


    }

    //测试
    @RequestMapping("/test/{goodsId}")
    @ResponseBody
    public Long dotest(@PathVariable(value = "goodsId") Long goodsId){
        log.info("id={}",goodsId);
        return goodsId;
    }


    /*秒杀页面静态化*/
    @RequestMapping(value = "/doSecKill/{goodsId}")
    @ResponseBody
    public RespBean doSecKill( User user, @PathVariable(value = "goodsId") Long goodsId) throws IOException {

//         参数解析器不起作用在云服务器上 可能是cookie的原因 也可能是其他原因
        String uuid=userService.getUuid();
        user = JsonUtil.jsonStr2obj(redisTemplate.opsForValue().get(uuid).toString(),User.class);
        if(user==null){
            return RespBean.error(RespBeanEnum.SESSION_TIMEOUT);
        }
        log.info("goodsId={}",goodsId);
        //      判断订单是否一个商品被重复购买   根据秒杀订单表里面的用户id和商品id查询订单 能查到就是这个用户已经买过了该商品,返回一个Err信息
        if(redisTemplate.hasKey("order:" + user.getId() + ":" + goodsId))
        {
            SeckillOrder  seckillOrder =JsonUtil.jsonStr2obj((String) redisTemplate.opsForValue().get("order:" + user.getId() + ":" + goodsId),SeckillOrder.class);
            if (seckillOrder!=null) {
//                return RespBean.error(RespBeanEnum.REBUY_ERROR);
            }
        }
//
//        boolean check=orderService.checkPath(user,goodsId,path);
//        if(!check){
//            return RespBean.error(RespBeanEnum.REQUEST_FAIL);
//        }
//   SeckillOrder  seckillOrder = (SeckillOrder) redisTemplate.opsForValue().get("order:" + user.getId() + ":" + goodsId);


        if (EmptyStockMap.get(goodsId)){
            return RespBean.error(RespBeanEnum.EMPTY_STOCK);
        }
//        预减库存
        ValueOperations valueOperations=redisTemplate.opsForValue();
//       原子性递减
        Long stock = valueOperations.decrement("seckillGoods:" + goodsId);
        if (stock<0){
//            加一使库存为0
            valueOperations.increment("seckillGoods:" + goodsId);
            EmptyStockMap.put(goodsId,true);//内存标记为true,则说明库存不足，直接不再进行redis抢购判断
            return RespBean.error(RespBeanEnum.EMPTY_STOCK);
        }
//        请求入队，立即返回正在排队中
        SeckillMessage seckillMessage = new SeckillMessage(user, goodsId);
        mqSender.sendSeckillMessage(JsonUtil.obj2JsonStr(seckillMessage));
        return RespBean.success(0);
    }

//    系统初始化执行的方法，把商品库存加载到redis中
    @Override
    public void afterPropertiesSet() throws Exception {
        List<GoodsVo> list = goodsService.findGoodsVo();
        if (CollectionUtils.isEmpty(list)){
            return;
        }
//        全部存到redis中去
        list.forEach(goodsVo ->
        {
            redisTemplate.opsForValue().set("seckillGoods:" +goodsVo.getId().toString(),goodsVo.getStockCount().toString());
            EmptyStockMap.put(goodsVo.getId(),false);
        });
    }

    /**
     * 获取秒杀结果
     * @param user
     * @param goodsId
     * @return
     */
    @RequestMapping(value = "/result",method = RequestMethod.GET)
    @ResponseBody
    public RespBean getResult(User user,Long goodsId) throws IOException {
        System.out.println("12314124");
        //         参数解析器不起作用在云服务器上 可能是cookie的原因 也可能是其他原因
        String uuid=userService.getUuid();
        user = JsonUtil.jsonStr2obj(redisTemplate.opsForValue().get(uuid).toString(),User.class);
        if (user==null){
            return RespBean.error(RespBeanEnum.SESSION_TIMEOUT);
        }
           Long orderId=   seckillOrderService.getResult(user,goodsId);
        System.out.println("orderid="+orderId);
        return RespBean.success(orderId);
    }


/**
 * 获取秒杀地址
 */
//    @AccessLimit(second=5,maxCount=5,needLogin=true)
    @RequestMapping(value = "/path",method = RequestMethod.GET)
    @ResponseBody
    public RespBean getPath(User user,@RequestParam("goodsId") Long goodsId,@RequestParam(value="captcha",defaultValue="0")String captcha,HttpServletRequest request) throws IOException {
        //         参数解析器不起作用在云服务器上 可能是cookie的原因 也可能是其他原因
        String uuid=userService.getUuid();
        user = JsonUtil.jsonStr2obj(redisTemplate.opsForValue().get(uuid).toString(),User.class);
        if(user==null){
                 return RespBean.error(RespBeanEnum.SESSION_TIMEOUT);
             }
//             接口限流-计数器实现，redis
        String uri = request.getRequestURI();

        Integer count= (Integer) redisTemplate.opsForValue().get(uri+":"+user.getId());
        String user_id=user.getId().toString();
        if (count==null){
            redisTemplate.opsForValue().set("uri:"+user_id,uri);
        }
        else if (count<10){
            redisTemplate.opsForValue().increment(uri+":"+user.getId());
        }
        else {
            return RespBean.error(RespBeanEnum.REQUEST_MULT);
        }
        boolean checkPath = orderService.checkCaptcha(user, goodsId, captcha);
        if(!checkPath){
            return RespBean.error(RespBeanEnum.ERROR_CAPTCHA);
        }
        String str=orderService.createPath(user,goodsId);
             return RespBean.success(str);
    }

    /**
     * 验证码
     * @param user
     * @param goodsId
     * @param response
     */

    @RequestMapping(value = "/captcha", method = RequestMethod.GET)
    public void verifyCode(User user, Long goodsId, HttpServletResponse response) throws IOException {
//         参数解析器不起作用在云服务器上 可能是cookie的原因 也可能是其他原因
         String uuid=userService.getUuid();
         user = JsonUtil.jsonStr2obj(redisTemplate.opsForValue().get(uuid).toString(),User.class);
        if (null==user||goodsId<0){
            throw new GlobalException(RespBeanEnum.SESSION_TIMEOUT);
        }
        // 设置请求头为输出图片类型
        response.setContentType("image/jpg");
        response.setHeader("Pragma", "No-cache");
        response.setHeader("Cache-Control", "no-cache");
        response.setDateHeader("Expires", 0);
        //生成验证码，将结果放入redis
        ArithmeticCaptcha captcha = new ArithmeticCaptcha(130, 32, 3);

        redisTemplate.opsForValue().set("captcha:"+user.getId()+":"+goodsId,captcha.text
                (),300, TimeUnit.SECONDS);
        try {
            captcha.out(response.getOutputStream());
        } catch (IOException e) {
            log.error("验证码生成失败",e.getMessage());
        }
    }

}
