package com.muye.wp.embed.server.socket;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * Created by muye on 18/4/12.
 */
public class Server {

    private int port;
    private ServerSocket server;

    public void start() throws IOException{
        server = new ServerSocket(port);

        while (true){
            Socket client = server.accept();

        }
    }
}
