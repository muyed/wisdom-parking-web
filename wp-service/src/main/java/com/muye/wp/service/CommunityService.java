package com.muye.wp.service;

import com.muye.wp.dao.domain.Community;
import com.muye.wp.dao.domain.query.CommunityQuery;
import com.muye.wp.dao.page.Page;

import java.util.List;

/**
 * Created by muye on 18/2/28.
 */
public interface CommunityService {

    Community queryById(Long id);

    List<Community> queryByCondition(CommunityQuery query, Page page);

    void add(Community community);

    void update(Community community);

    void delete(Long id);
}
