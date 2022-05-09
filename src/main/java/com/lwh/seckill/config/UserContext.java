package com.lwh.seckill.config;

import com.lwh.seckill.entity.User;
//数据副本，threadlocal,线程专有的，存放user对象，避免线程安全问题
public class UserContext {
    private static ThreadLocal<User> userHolder = new ThreadLocal<>();
    public static void setUser(User user) {
        userHolder.set(user);
    }
    public static User getUser() {
        return userHolder.get();
    }
}

