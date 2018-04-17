package com.muye.wp.embed.server.lock.service;

import org.springframework.stereotype.Component;

/**
 * Created by muye on 18/4/17.
 */
@Component
public class LockEmbedServiceImpl implements LockEmbedService {

    @Override
    public boolean lock(String meid) {
        return true;
    }

    @Override
    public boolean unLock(String meid) {
        return true;
    }
}
