package com.muye.wp.embed.server.lock.service;

/**
 * Created by muye on 18/4/17.
 */
public interface LockEmbedService {

    boolean lock(String meid);

    boolean unLock(String meid);
}
