package com.muye.wp.embed.server.processor;

import com.muye.wp.common.cons.RespStatus;
import com.muye.wp.common.exception.WPException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * Created by muye on 18/2/26.
 *
 * 服务处理器
 * 接受工控机、地锁控制器发起的socket长连接请求 请处理请求
 */
public class Processor {

    private static final Logger logger = LoggerFactory.getLogger(Processor.class);

    //工作线程池
    private final ThreadPoolExecutor works = new ThreadPoolExecutor(
            Runtime.getRuntime().availableProcessors(),
            Runtime.getRuntime().availableProcessors(),
            0l,
            TimeUnit.MILLISECONDS,
            new LinkedBlockingDeque<>());

    private final int socketPort;
    private final ServerSocket serverSocket;
    private final ConcurrentHashMap<String, Socket> clientCache = new ConcurrentHashMap<>();

    private boolean isAccepted = false;

    public Processor (int socketPort){
        logger.info("初始化embed服务处理器。。。");
        this.socketPort = socketPort;
        try {
            this.serverSocket = new ServerSocket(socketPort);
        }catch (IOException e){
            throw new WPException(RespStatus.EMBED_SERVER_ERR, e);
        }

        //test
        try {
            Socket socket = serverSocket.accept();
            InputStream inputStream = socket.getInputStream();
            byte[] bytes = new byte[1024];
            int len;
            StringBuilder sb = new StringBuilder();
            while ((len = inputStream.read(bytes)) != -1) {
                //注意指定编码格式，发送方和接收方一定要统一，建议使用UTF-8
                sb.append(new String(bytes, 0, len,"UTF-8"));
            }
            System.out.println("get message from client: " + sb);
            inputStream.close();
            socket.close();
            serverSocket.close();
        }catch (Exception e){

        }
    }
}
