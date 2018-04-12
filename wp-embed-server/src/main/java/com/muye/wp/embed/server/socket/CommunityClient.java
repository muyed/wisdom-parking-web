package com.muye.wp.embed.server.socket;

import java.net.Socket;
import java.util.List;

/**
 * Created by muye on 18/4/12.
 */
public class CommunityClient {

    protected Long communityId;
    protected Long communityName;
    protected List<Socket> socketList;        //每个小区有多个门禁设备
}
