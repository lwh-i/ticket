package com.lwh.seckill.exception;

import com.lwh.seckill.vo.RespBeanEnum;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@NoArgsConstructor
@AllArgsConstructor
/*全局异常*/
public class GlobalException extends RuntimeException {


    private RespBeanEnum respBeanEnum;
}
