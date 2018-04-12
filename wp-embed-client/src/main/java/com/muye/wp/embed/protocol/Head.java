package com.muye.wp.embed.protocol;

import lombok.Data;

import java.util.concurrent.atomic.AtomicLong;

/**
 * Created by muye on 18/4/12.
 */
@Data
public class Head {

    private static final AtomicLong INVOKE_ID = new AtomicLong(1);

    //head 长度 = 1 + 8 + 1 + 1 + 4 = 15
    private final byte version = 1;     //协议版本号
    private long askId;                 //请求id

    /**
     * @see ProtoType
     */
    private byte type;                  //协议类型  请求/响应

    /**
     * @see ProtoMethod
     */
    private byte method;                //请求的方法
    private int size;                   //总长度 head + body

    public Head(){
        askId = INVOKE_ID.getAndIncrement();
    }
}
