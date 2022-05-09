package com.lwh.seckill.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.lwh.seckill.entity.SeckillOrder;
import com.lwh.seckill.entity.User;
import com.lwh.seckill.mapper.SeckillOrderMapper;
import com.lwh.seckill.service.ISeckillOrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author lwh
 * @since 2021-11-04
 */
@Service
public class SeckillOrderServiceImpl extends ServiceImpl<SeckillOrderMapper, SeckillOrder> implements ISeckillOrderService {

    @Autowired
    RedisTemplate redisTemplate;
    @Autowired
    SeckillOrderMapper seckillOrderMapper;

    /**
     *
     * @param user
     * @param goodsId
     * @return :orderid:成功   -1：失败    0：排队中
     */
    @Override
    public Long getResult(User user, Long goodsId) {

        SeckillOrder seckillOrder = seckillOrderMapper.selectOne(new QueryWrapper<SeckillOrder>().eq("user_id", user.getId()).eq("goods_id", goodsId));
        if (null!=seckillOrder){
            return seckillOrder.getGoodsId();
        }
        else if(redisTemplate.hasKey("isStockEmpty"+goodsId)){
            return -1L;
        }
        else{
            return 0L;
        }

    }

}
