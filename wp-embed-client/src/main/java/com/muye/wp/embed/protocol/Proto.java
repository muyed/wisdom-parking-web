package com.muye.wp.embed.protocol;

import lombok.Data;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicLong;

/**
 * Created by muye on 18/4/12.
 *
 * 服务端推消息给小区门禁嵌入式端协议
 */
@Data
public class Proto implements Serializable {

    private static final AtomicLong INVOKE_ID = new AtomicLong(1);

    private byte version = 1;           //协议版本号
    private long askId;                 //请求id
    private ProtoType type;             //协议类型  请求/响应
    private ProtoMethod method;         //请求的方法
    private int size;                   //body size

    //body
    private List<Object> body;          //请求/响应体


    public Proto (){
        this.askId = INVOKE_ID.getAndIncrement();
    }

    public Proto putType(ProtoType type){
        this.type = type;
        return this;
    }

    public Proto putMethod(ProtoMethod method){
        this.method = method;
        return this;
    }

    public Proto addBodyItem(Object bodyItem){
        if (body == null) body = new ArrayList<>();
        body.add(bodyItem);
        return this;
    }
}
