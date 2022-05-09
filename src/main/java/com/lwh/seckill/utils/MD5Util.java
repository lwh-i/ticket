package com.lwh.seckill.utils;

import org.apache.commons.codec.digest.DigestUtils;


public class MD5Util {
    public static String md5(String str) {
        return DigestUtils.md5Hex(str);
    }

    private static final String salt = "1234";

    public static String inputPassToFromPass(String input) {
        String str =""+salt.charAt(0)+salt.charAt(2)+input;

        return md5(str);

    }

//一次加密
    public static String formPassToDB(String formPass, String salt) {
        String str = ""+salt.charAt(0) + salt.charAt(2) + formPass;
        System.out.println("pass="+md5(str));
        return md5(str);
    }
//    直接两次加密
    public static String inputToDBpass(String inputPass,String salt){
        String fromPass=inputPassToFromPass(inputPass);
        String dbPass=formPassToDB(fromPass,salt);
        return dbPass;
    }

    public static void main(String[] args) {
        String inputToF=inputPassToFromPass("123456");
        System.out.println(inputToF+"--");
        System.out.println(formPassToDB(inputToF,"1234"));
        System.out.println(inputToDBpass("123456","1234"));
    }
}
