package com.muye.wp.embed.protocol;

import lombok.Data;

import java.io.Serializable;
import java.util.List;
import java.util.concurrent.atomic.AtomicLong;

/**
 * Created by muye on 18/4/12.
 *
 * 服务端推消息给小区门禁嵌入式端协议
 */
@Data
public class Proto implements Serializable {

    private Head head;

    //body
    private List<Object> body;          //请求/响应体


    public Proto (Head head, List<Object> body){
        this.head = head;
        this.body = body;
    }
}
