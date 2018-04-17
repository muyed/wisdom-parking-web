package com.muye.wp.embed.server.door.core;

import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by muye on 18/4/12.
 */
public class CommunityClient {

    volatile protected boolean isReading = false;
    protected Long communityId;
    protected String communityName;
    protected HashMap<String, Socket> socketList;        //每个小区有多个门禁设备

    public CommunityClient (Long communityId, String communityName){
        this.communityId = communityId;
        this.communityName = communityName;
        socketList = new HashMap<>();
    }

    public void addClient(String clientNum, Socket socket){
        socketList.put(clientNum, socket);
    }

    public List<Socket> getSocketList(){
        return new ArrayList<>(socketList.values());
    }
}
