package com.lwh.seckill.rabbitmq;

import com.lwh.seckill.entity.SeckillMessage;
import com.lwh.seckill.entity.SeckillOrder;
import com.lwh.seckill.entity.User;
import com.lwh.seckill.service.IGoodsService;
import com.lwh.seckill.service.IOrderService;
import com.lwh.seckill.service.impl.OrderServiceImpl;
import com.lwh.seckill.utils.JsonUtil;
import com.lwh.seckill.vo.GoodsVo;
import lombok.extern.slf4j.Slf4j;
import org.apache.logging.log4j.message.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
@Slf4j
public class MQReceiver {
//    这个注解时监听一个队列
//    @RabbitListener(queues = "queue")
//    public void receive(Object msg){
//        log.info("receive:"+msg);
//    }
//    @RabbitListener(queues = "queue_fanout01")
//    public void receive01(Object msg){
//        log.info("receive01:"+msg);
//    }
//    @RabbitListener(queues = "queue_fanout02")
//    public void receive02(Object msg){
//        log.info("receive02:"+msg);
//    }


    @Autowired
    IGoodsService goodsService;
    @Autowired
    RedisTemplate redisTemplate;
    @Autowired
    IOrderService orderService;


    //    接收消息下单操作
    @RabbitListener(queues = "seckillQueue")
    public void receive(String message) throws IOException {
        log.info("receive:" + message);
        SeckillMessage seckillMessage = JsonUtil.jsonStr2obj(message, SeckillMessage.class);
        Long goodID = seckillMessage.getGoodID();
        User user = seckillMessage.getUser();
//        判断库存
        GoodsVo goodsVo = goodsService.findGoodsVoByGoodsId(goodID);
        if (goodsVo.getStockCount() < 1) {
            return;
        }
//        判断重复抢购
        if(redisTemplate.hasKey("order:" + user.getId() + ":" + goodID)){
            SeckillOrder seckillOrder =  JsonUtil.jsonStr2obj((String) redisTemplate.opsForValue().get("order:" + user.getId() + ":" + goodID),SeckillOrder.class);
//        SeckillOrder seckillOrder = (SeckillOrder) redisTemplate.opsForValue().get("order:" + user.getId() + ":" + goodID);
            if (seckillOrder != null) {
                return;
            }
        }

//        开始下单
        orderService.seckill(user, goodsVo);
    }

}
