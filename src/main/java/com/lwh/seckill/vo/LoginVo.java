package com.lwh.seckill.vo;

import com.lwh.seckill.validator.IsMobile;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotNull;

@Data
public class LoginVo {
//    @NotNull
//    @IsMobile
    private String phone;
//    @NotNull
//    @Length(min = 4)
    private String password;
}
