package com.muye.wp.embed.server.core;

import com.alibaba.fastjson.JSONObject;
import com.muye.wp.dao.domain.Community;
import com.muye.wp.dao.mapper.CommunityMapper;
import com.muye.wp.embed.codec.Codec;
import com.muye.wp.embed.protocol.Proto;
import com.muye.wp.embed.protocol.ProtoMethod;
import com.muye.wp.embed.protocol.ProtoType;
import com.muye.wp.embed.server.service.EmbedService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.InputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by muye on 18/4/12.
 */
@Component
public class Server implements InitializingBean {

    private static final Logger logger = LoggerFactory.getLogger(Server.class);

    private static final ConcurrentHashMap<Long, CommunityClient> clientCache = new ConcurrentHashMap<>();

    private final ExecutorService executorService = Executors.newFixedThreadPool(3);
    protected static final ConcurrentHashMap<Long, ArrayList<Proto>> respCache = new ConcurrentHashMap<>();

    @Autowired(required = false)
    private CommunityMapper communityMapper;

    @Autowired
    private EmbedService embedService;

    private int port = 5555;
    private ServerSocket server;

    @Override
    public void afterPropertiesSet() throws Exception {
        start();
    }

    public void start() throws IOException{
        server = new ServerSocket(port);

        //单独线程监听客户端的注册请求
        new Thread(() -> {
            for (;;){
                try {
                    synchronized (this){
                        reg(server.accept());
                    }
                }catch (Exception e){
                }
            }
        }).start();

        //处理客户端信息
        for (int i = 0; i < 3; i++){
            executorService.execute(() -> {
                for (;;){
                    clientCache.values().forEach(client -> {
                        synchronized (client){
                            if (!client.isReading){
                                client.isReading = true;
                                client.socketList.values().forEach(socket -> {
                                    try {
                                        InputStream in = socket.getInputStream();
                                        Proto proto = Codec.decode(in);
                                        if (proto.getType() == ProtoType.ANSWER){
                                            List<Proto> list = respCache.get(proto.getAskId());
                                            list.add(proto);
                                        }
                                    }catch (Exception e){
                                    }
                                });
                                client.isReading = false;
                            }
                        }
                    });
                }
            });
        }
    }

    private void reg(Socket socket){
        try {
            Proto proto = Codec.decode(socket.getInputStream());
            if (proto.getType() == ProtoType.ASK && proto.getMethod() == ProtoMethod.INIT){
                Long communityId = (Long) proto.getBody().get(0);
                String communityName = (String) proto.getBody().get(1);
                String clientNum = (String) proto.getBody().get(2);

                logger.info("reg -> {}, {}", communityId, communityName);

                Community community = communityMapper.selectById(communityId);
                if (community != null && community.getCommunityName().equals(communityName)){
                    CommunityClient client = clientCache.get(communityId);
                    if (client == null) client = new CommunityClient(communityId, communityName);
                    client.addClient(clientNum, socket);
                    logger.info("注册成功 -> {}, {}", communityId, communityName);

                    CommunityClient communityClient = clientCache.get(communityId);
                    if (communityClient == null) communityClient = new CommunityClient(communityId, communityName);

                    clientCache.put(communityId, communityClient);
                    communityClient.addClient(clientNum, socket);

                    boolean result = embedService.refreshCommunityDate(communityId);
                    if (result) logger.info("{}:写入小区数据成功", communityId);
                    else logger.error("{}:写入小区数据失败", communityId);
                    return;
                }
            }

            logger.warn("注册失败 -> {}", JSONObject.toJSONString(proto));
            socket.close();
        }catch (Exception e){
            e.printStackTrace();
            try {
                if (socket != null) socket.close();
            }catch (Exception e1){
            }
        }
    }

    public static CommunityClient getClient(Long communityId){
        return clientCache.get(communityId);
    }
}
