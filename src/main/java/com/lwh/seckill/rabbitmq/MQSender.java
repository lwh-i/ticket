package com.lwh.seckill.rabbitmq;


import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class MQSender {
    @Autowired
    private RabbitTemplate rabbitTemplate;
//    public void send(Object obj){
//        log.info("send:"+obj);
////        rabbitTemplate.convertAndSend("queue",obj);
//        rabbitTemplate.convertAndSend("fanout_exchange","",obj);
//
//    }
//    发送秒杀消息
    public void sendSeckillMessage(String message){
        log.info("发送消息："+message);
        rabbitTemplate.convertAndSend("seckillExchange","seckill.message",message);
    }
}
