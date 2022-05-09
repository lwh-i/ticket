package com.lwh.seckill.utils;

public class Te {
    public static void main(String[] args) {
        String str="12345";
        String s1=""+str.charAt(0)+str.charAt(2);
        System.out.println(s1);
        String a1="212421";
        int re=a1.charAt(0)+a1.charAt(1);
        System.out.println("re="+re);
        int new_a1 = Integer.parseInt(a1);
        int a2=1;
        System.out.println(new_a1+" -"+a2);
        System.out.println(a2+new_a1);
    }
}
