package com.muye.wp.service;

import com.muye.wp.dao.domain.CommunityModule;
import com.muye.wp.dao.domain.query.CommunityModuleQuery;
import com.muye.wp.dao.page.Page;

import java.util.List;

/**
 * Created by muye on 18/3/1.
 */
public interface CommunityModuleService {

    CommunityModule queryById(Long id);

    List<CommunityModule> queryByCondition(CommunityModuleQuery query, Page page);

    void add(CommunityModule communityModule);

    void update(CommunityModule communityModule);

    void delete(Long id);
}
