package com.muye.wp.embed.client.core;

import java.io.*;
import java.net.Socket;
import java.util.Properties;

/**
 * Created by muye on 18/4/12.
 */
public class Client {

    private static Client client;

    private String serverIp;
    private int port;
    private Long communityId;
    private String communityName;
    protected InputStream in;
    protected OutputStream out;

    private Socket socket;

    private Client (){

    }

    public void start() throws IOException{
        socket = new Socket(serverIp, port);
        in = socket.getInputStream();
        out = socket.getOutputStream();
    }

    public static synchronized Client instance() throws Exception{
        if (client == null){
            InputStream in = new FileInputStream(new File("/usr/local/wp/wp-embed-client/config/application.properties"));
            Properties properties = new Properties();
            properties.load(in);

            client = new Client();

            client.serverIp = properties.getProperty("server.ip");
            client.port = Integer.valueOf(properties.getProperty("server.port"));
            client.communityId = Long.valueOf(properties.getProperty("community.id"));
            client.communityName = properties.getProperty("community.name");

            if (client.communityId == null) throw new RuntimeException("小区id不能为空");
            if (client.communityName == null || client.communityName.length() == 0) throw new RuntimeException("小区名称不能为空");
        }

        return client;
    }
}
