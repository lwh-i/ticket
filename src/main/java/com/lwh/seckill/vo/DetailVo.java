package com.lwh.seckill.vo;

import com.lwh.seckill.entity.User;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

//详情返回对象
@Data
@NoArgsConstructor
@AllArgsConstructor
public class DetailVo {
    private User user;
    private  GoodsVo goodsVo;
    private  int secKillStatus;
    private int remainSeconds;
}
