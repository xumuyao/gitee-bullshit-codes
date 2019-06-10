package com.test.utils;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class Great {
    public static void main(String[] args) throws IOException {
        Map<String,Object> map = new HashMap<>();//或者JSON
        /*
         *数据初始化省略
         */
        String str = "new str";
        //满足某个条件时，尝试修改set集合，因为只有对key值的需求，所以并未修改map
        Set<String> set = map.keySet();
        if(true){
            //这里会有惊喜
            set.add(str);
        }
    }
}
