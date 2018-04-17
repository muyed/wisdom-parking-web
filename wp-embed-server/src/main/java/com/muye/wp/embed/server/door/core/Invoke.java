package com.muye.wp.embed.server.door.core;

import com.muye.wp.embed.codec.Codec;
import com.muye.wp.embed.protocol.Proto;

import java.io.OutputStream;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

/**
 * Created by muye on 18/4/13.
 */
public class Invoke {

    private Proto proto;
    private List<Socket> sockets;

    public Invoke (Proto proto, List<Socket> sockets){
        this.proto = proto;
        this.sockets = sockets;
    }

    public List<Object> invoke() throws Exception{

        byte[] bytes = Codec.encode(proto);
        Server.respCache.put(proto.getAskId(), new ArrayList<>());

        sockets.forEach(socket -> {
            try {
                OutputStream out = socket.getOutputStream();
                out.write(bytes);
                out.flush();
            }catch (Exception e){
            }
        });

        List<Proto> respList = getResult().get(10, TimeUnit.SECONDS);
        List<Object> result = new ArrayList<>(respList.size());
        respList.forEach(proto -> result.add(proto.getBody().get(0)));
        return result;
    }

    private Future<List<Proto>> getResult(){
        return Executors.newSingleThreadExecutor().submit(() -> {
            for (;;){
                if (Server.respCache.get(proto.getAskId()).size() == sockets.size()){
                    return Server.respCache.get(proto.getAskId());
                }
            }
        });
    }
}
