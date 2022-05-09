package com.lwh.seckill.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.lwh.seckill.entity.SeckillOrder;
import com.lwh.seckill.entity.User;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author lwh
 * @since 2021-11-04
 */
public interface ISeckillOrderService extends IService<SeckillOrder> {
    /**
     * 获取秒杀结果
     * @param user
     * @param goodsId
     * @return  :orderid:成功   -1：失败    0：排队中
     */
    Long getResult(User user, Long goodsId);
}
