package com.lwh.seckill.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.lwh.seckill.entity.Order;
import com.lwh.seckill.entity.User;
import com.lwh.seckill.vo.GoodsVo;
import com.lwh.seckill.vo.OrderDetailVo;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author lwh
 * @since 2021-11-04
 */
public interface IOrderService extends IService<Order> {
//秒杀
    Order seckill(User user, GoodsVo goodsVo);

    OrderDetailVo detail(Long orderId,User user);

    /**
     * 获取秒杀地址
     * @param user
     * @param goodsId
     * @return
     */
    String createPath(User user, Long goodsId);
//校验秒杀地址
    boolean checkPath(User user, Long goodsId,String path);
//校验验证码
    boolean checkCaptcha(User user, Long goodsId, String captcha);
}
