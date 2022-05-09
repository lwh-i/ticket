package com.lwh.seckill.exception;


import com.lwh.seckill.vo.RespBean;
import com.lwh.seckill.vo.RespBeanEnum;
import org.springframework.validation.BindException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;



/*全局异常处理类*/
//@RestControllerAdvice
//public class GlobalExceptionHandler {
//
//    @ExceptionHandler(Exception.class)
//    public RespBean ExceptionHandler(Exception e){
//        if(e instanceof  GlobalException){
//            GlobalException ex=(GlobalException) e;
//        }
//        else if(e instanceof BindException){
//            BindException ex=(BindException) e;
//            RespBean respBean=RespBean.error(RespBeanEnum.ARG_ERROR);
//            respBean.setMessage("参数异常"+ex.getBindingResult().getAllErrors().get(0).getDefaultMessage());
//            return respBean;
//        }
//        return  RespBean.error(RespBeanEnum.ERROR);
//    }
//
//}
