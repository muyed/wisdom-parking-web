package com.muye.wp.embed.server.socket;

import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by muye on 18/4/12.
 */
public class Context {

    protected static final ConcurrentHashMap<Long, CommunityClient> clientCache = new ConcurrentHashMap<>();


}
