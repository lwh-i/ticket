package com.lwh.seckill.controller;

import com.lwh.seckill.entity.User;
import com.lwh.seckill.service.IGoodsService;
import com.lwh.seckill.service.UserService;
import com.lwh.seckill.vo.DetailVo;
import com.lwh.seckill.vo.GoodsVo;
import com.lwh.seckill.vo.RespBean;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.thymeleaf.context.WebContext;
import org.thymeleaf.spring5.view.ThymeleafViewResolver;
import org.thymeleaf.util.StringUtils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;
/*跳转商品列表页*/

@Controller
@Slf4j
@RequestMapping("/goods")
public class GoodsController {
    @Autowired
    private UserService userService;
    @Autowired
    private IGoodsService goodsService;
    @Autowired
    RedisTemplate redisTemplate;
    @Autowired
    ThymeleafViewResolver thymeleafViewResolver;

    @RequestMapping(value = "/toList", produces = "text/html;charset=utf-8")
    @ResponseBody
    public String toList(Model model, User user, HttpServletRequest request, HttpServletResponse response) {
        ValueOperations valueOperations = redisTemplate.opsForValue();
        String html = (String) valueOperations.get("goodsList");
        if (!StringUtils.isEmpty(html)) {
            return html;
        }
        model.addAttribute("user", user);
        List<GoodsVo> goodsVo = goodsService.findGoodsVo();
        if(goodsVo.isEmpty()){
            return "";
        }
        model.addAttribute("goodsList", goodsVo);
        log.info("goodsVo={}",goodsVo);
        WebContext webContext = new WebContext(request, response, request.getServletContext(), request.getLocale(), model.asMap());
        html = thymeleafViewResolver.getTemplateEngine().process("goodsList", webContext);
        if (!StringUtils.isEmpty(html)) {
            valueOperations.set("goodsList", html, 30, TimeUnit.SECONDS);
        }
        return html;

    }

    /**
     *查询车票信息列表
     **/
    @RequestMapping("/getList")
    @ResponseBody
    public List<GoodsVo> getGoodList(){
        List<GoodsVo> goodsVo = goodsService.findGoodsVo();
        log.info("goodsList:{}",goodsVo);
        return goodsVo;
    }


    /*商品详情页
     根据商品id获取商品详情
        页面缓存的时候用,对象缓存不用*/
    @RequestMapping(value = "/toDetail2/{goodsId}", produces = "text/html;charset=utf-8")
    @ResponseBody
    public String toDetail2(Model model, User user, @PathVariable Long goodsId, HttpServletRequest request, HttpServletResponse response) {
//       判断redis缓存中是否有页面，有直接返回，否则手动渲染
        ValueOperations valueOperations = redisTemplate.opsForValue();
        String html = (String) valueOperations.get("goodsDetail:" + goodsId);
        if (!StringUtils.isEmpty(html)) {
            return html;
        }
        model.addAttribute("user", user);
        GoodsVo goodsVo = goodsService.findGoodsVoByGoodsId(goodsId);
        Date startDate = goodsVo.getStartDate();
        Date endDate = goodsVo.getEndDate();
        System.out.println(goodsVo);
        Date nowDate = new Date();
//    秒杀状态
        int seckillStatus = 0;
//    秒杀倒计时
        int remainSeconds = 0;
//    秒杀还未开始
        if (nowDate.before(startDate)) {
            System.out.println("秒杀还没开始");
            remainSeconds = (int) ((startDate.getTime() - nowDate.getTime()) / 1000);
        }
//    秒杀已经结束
        else if (nowDate.after(endDate)) {
            System.out.println("秒杀已经结束");
            seckillStatus = 2;
            remainSeconds = -1;
        } else {
            System.out.println("正在秒杀");
            seckillStatus = 1;
            remainSeconds = 0;

        }
        model.addAttribute("seckillStatus", seckillStatus);
        model.addAttribute("remainSeconds", remainSeconds);
        model.addAttribute("goods", goodsVo);
//        手动渲染  存入redis并返回
        WebContext webContext = new WebContext(request, response, request.getServletContext(), request.getLocale(), model.asMap());
        html = thymeleafViewResolver.getTemplateEngine().process("goodsDetail", webContext);
        if(!StringUtils.isEmpty(html)){
            valueOperations.set("goodsDetail:"+goodsId,html,60,TimeUnit.SECONDS);
        }

        return html;

    }


    /*商品详情页
     根据商品id获取商品详情
        对象缓存*/
    @RequestMapping("/detail/{goodsId}")
    @ResponseBody
    public RespBean toDetail(User user, @PathVariable Long goodsId) {
        System.out.println("1111");
        GoodsVo goodsVo = goodsService.findGoodsVoByGoodsId(goodsId);
//        model.addAttribute("user",user);

        Date startDate = goodsVo.getStartDate();
        Date endDate = goodsVo.getEndDate();
        System.out.println(goodsVo);
        Date nowDate = new Date();
//    秒杀状态
        int seckillStatus = 0;
//    秒杀倒计时
        int remainSeconds = 0;
//    秒杀还未开始
        if (nowDate.before(startDate)) {
            System.out.println("秒杀还没开始");
            remainSeconds = (int) ((startDate.getTime() - nowDate.getTime()) / 1000);
        }
//    秒杀已经结束
        else if (nowDate.after(endDate)) {
            System.out.println("秒杀已经结束");
            seckillStatus = 2;
            remainSeconds = -1;
        } else {
            System.out.println("正在秒杀");
            seckillStatus = 1;
            remainSeconds = 0;

        }
//        model.addAttribute("secKillStatus",seckillStatus);
//        model.addAttribute("remailSeconds",remainSeconds);
//        model.addAttribute("goods",goodsVo);
        DetailVo detailVo = new DetailVo();
        detailVo.setUser(user);
        detailVo.setGoodsVo(goodsVo);
        detailVo.setSecKillStatus(seckillStatus);
        detailVo.setRemainSeconds(remainSeconds);



        return RespBean.success(detailVo);

    }
}
