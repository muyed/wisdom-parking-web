package com.muye.wp.service;

import com.muye.wp.dao.domain.Carport;
import com.muye.wp.dao.page.Page;

import java.util.List;

/**
 * Created by muye on 18/3/6.
 */
public interface CarportService {

    void add(Carport carport);

    String refreshBindCode(Long id);

    List<Carport> queryListByCondition(Carport query, Page page);

    /**
     * 查找某个用户某个小区的所有车位
     */
    List<Carport> queryListByUserIdAndCommunityId(Long userId, Long communityId);

    Carport queryById(Long id);

    void update(Carport carport);

    void lock(Long userId, Long carportId);

    void unLock(Long userId, Long carportId);
}
