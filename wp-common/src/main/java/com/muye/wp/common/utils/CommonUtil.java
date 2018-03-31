package com.muye.wp.common.utils;

import com.muye.wp.common.cons.ProductType;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;

/**
 * Created by muye on 18/1/28.
 */
public class CommonUtil {

    public static String genRandomNum(int length){
        Random random = new Random();
        String s = "";
        for (int i = 0; i < length; i++){
            s = s + random.nextInt(10);
        }
        return s;
    }

    public static String genPayNum(ProductType type){
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
        StringBuffer sb = new StringBuffer(type.getCode());
        sb.append(sdf.format(new Date()));
        sb.append(genRandomNum(6));
        return sb.toString();
    }
}
