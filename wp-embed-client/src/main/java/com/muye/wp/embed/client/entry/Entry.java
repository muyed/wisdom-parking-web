package com.muye.wp.embed.client.entry;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.util.Properties;

/**
 * Created by muye on 18/2/27.
 *
 * 入口
 */
public class Entry {

    public static void main(String[] args) throws Exception{

        //读取配置文件
        InputStream in = new FileInputStream(new File("/usr/local/wp/wp-embed-client/config/application.properties"));
        Properties properties = new Properties();
        properties.load(in);

        String embedServerIp = properties.getProperty("embed.ip");
        String embedServerPort = properties.getProperty("embed.port");

        if (null == embedServerIp || embedServerIp.length() == 0){
            throw new RuntimeException("配置embed.ip不能为空");
        }

        if (null == embedServerPort || embedServerPort.length() == 0){
            throw new RuntimeException("配置embed.port不能为空");
        }

        System.out.println("embedServerIp: " + embedServerIp + ", embedServerPort: " + embedServerPort);

        //test
        Socket socket = new Socket(embedServerIp, Integer.valueOf(embedServerPort));
        OutputStream outputStream = socket.getOutputStream();
        outputStream.write("哈哈".getBytes("utf-8"));
        outputStream.flush();
        outputStream.close();
        socket.close();
    }
}
