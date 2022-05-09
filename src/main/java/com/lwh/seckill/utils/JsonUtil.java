package com.lwh.seckill.utils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.List;

public class JsonUtil {
    private static ObjectMapper objectMapper=new ObjectMapper();
//    对象转json字符串
    public static String obj2JsonStr(Object obj){
        try {
            return  objectMapper.writeValueAsString(obj);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return null;
    }
//    json字符串转对象
    public static <T> T jsonStr2obj(String jsonstr,Class<T> clazz) throws IOException {
        return objectMapper.readValue(jsonstr.getBytes("UTF-8"),clazz);
    }
//    将json字符串转pojo对象list
    public static <T>List<T> json2list(String json,Class<T> beanType){
        JavaType javaType=objectMapper.getTypeFactory().constructParametricType(List.class,beanType);
        try{
            List<T> list=objectMapper.readValue(json,javaType);
            return list;
        } catch (JsonMappingException e) {
            e.printStackTrace();
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return null;
    }
}
