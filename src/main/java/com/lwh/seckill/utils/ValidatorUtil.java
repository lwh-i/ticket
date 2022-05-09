package com.lwh.seckill.utils;

import org.thymeleaf.util.StringUtils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/*手机号码校验类*/
public class ValidatorUtil {
    private static final Pattern mobile_pattern=Pattern.compile("[1]([3-9])[0-9]{9}$");


    public static boolean isMobile(String phone) {
        if(StringUtils.isEmpty(phone)){
            return false;
        }
        Matcher matcher=mobile_pattern.matcher(phone);
        return matcher.matches();
    }
}
