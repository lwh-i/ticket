//package com.lwh.seckill.iterceptor;
//
//import org.springframework.boot.SpringBootConfiguration;
//import org.springframework.web.servlet.config.annotation.CorsRegistry;
//import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
//
///**
// * @author liuwenhao
// * @date 2022/02/24
// */
//@SpringBootConfiguration
//public class MyWebConfigurer implements WebMvcConfigurer {
//    @Override
//    public void addCorsMappings(CorsRegistry corsRegistry){
//        /**
//         * 所有请求都允许跨域，使用这种配置就不需要
//         * 在interceptor中配置header了
//         */
//        corsRegistry.addMapping("/**")
//                .allowCredentials(true)
//                .allowedOrigins("http://localhost:8888")
//                .allowedMethods("POST", "GET", "PUT", "OPTIONS", "DELETE")
//                .allowedHeaders("*")
//                .maxAge(3600);
//    }
//}
