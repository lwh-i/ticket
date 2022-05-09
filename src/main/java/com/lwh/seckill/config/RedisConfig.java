package com.lwh.seckill.config;

import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

/*redis 配置类*/
@Configuration
@EnableAutoConfiguration
public class RedisConfig {
    @Bean
    public RedisTemplate<String, Object> redisTemplate(RedisConnectionFactory redisConnectionFactory) {
        RedisTemplate<String, Object> redisTemplate = new RedisTemplate<>();
        RedisSerializer stringSerializer = new StringRedisSerializer();
        //        注入连接工厂
        redisTemplate.setConnectionFactory(redisConnectionFactory);
//        key序列化
        redisTemplate.setKeySerializer(stringSerializer);
//        value的序列化
        redisTemplate.setValueSerializer(stringSerializer);
//        hash类型的key序列化
        redisTemplate.setHashKeySerializer(stringSerializer);
//                hash类型val的序列化
        redisTemplate.setHashValueSerializer(stringSerializer);


        return redisTemplate;
    }
}
