package com.lwh.seckill.config;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)//运行时
@Target(ElementType.METHOD)
public @interface AccessLimit {
  int second();
  int maxCount();
  boolean needLogin() default true;
}
