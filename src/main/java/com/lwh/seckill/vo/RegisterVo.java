package com.lwh.seckill.vo;

import com.lwh.seckill.validator.IsMobile;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
@Data
public class RegisterVo {
    @IsMobile
    long phone;
    @Length(min = 6)
    String password;
    @NotNull
    String username;
}
