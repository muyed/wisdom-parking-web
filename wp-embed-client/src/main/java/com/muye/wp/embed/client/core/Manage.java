package com.muye.wp.embed.client.core;

import com.muye.wp.embed.codec.Codec;
import com.muye.wp.embed.protocol.Proto;
import com.muye.wp.embed.protocol.ProtoMethod;
import com.muye.wp.embed.protocol.ProtoType;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by muye on 18/4/12.
 */
public class Manage {

    private Client client;
    private Router router = new Router();

    public void start() throws Exception{
        try {
            client = Client.instance();
            client.clean();
            client.start();

            reg();

            while(true) {
                Proto proto = Codec.decode(client.in);

                //处理响应
                if (proto.getType() == ProtoType.ANSWER){
                    System.out.println("响应---> " + proto.getBody().stream().reduce((a, b) -> a.toString() + b.toString()));
                    continue;
                }

                //处理请求
                Object resp = router.router(proto);
                List<Object> body = new ArrayList<>();
                body.add(resp);
                proto.setBody(body);
                proto.setType(ProtoType.ANSWER);
                byte[] bytes = Codec.encode(proto);
                client.out.write(bytes);
                client.out.flush();
            }
        }catch (Exception e){
            System.out.println("断开联系 重试中");
            Thread.sleep(10 * 1000);
            start();
        }
    }

    private void reg() throws Exception{
        Proto proto = new Proto();
        proto.setType(ProtoType.ASK);
        proto.setMethod(ProtoMethod.INIT);

        List<Object> body = new ArrayList<>();
        body.add(client.communityId);
        body.add(client.communityName);
        body.add(client.clientNum);
        proto.setBody(body);

        byte[] bytes = Codec.encode(proto);
        client.out.write(bytes);
        client.out.flush();
    }
}
