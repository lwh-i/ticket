package com.lwh.seckill;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class SeckillDemoApplicationTests {

    @Test
    void contextLoads() {
    }

    @Test
    void Te1(){
        int arr[]={1,2,4,1,6,2,3};
        quick_sort(0,arr.length,arr);
        System.out.println(arr);
    }


    void  quick_sort(int low,int height,int arr[]){
        int left=low;
        int right=height;
        int temp=arr[left];
        while (left<right){
            while (left<right&&arr[right]>=temp){
                right--;
            }
            arr[left]=arr[right];
            while (left<right&&arr[left]<temp){
                left++;
            }
            arr[right]=arr[left];
        }
        arr[left]=temp;
        quick_sort(low,left-1,arr);
        quick_sort(left+1,height,arr);

    }


}
