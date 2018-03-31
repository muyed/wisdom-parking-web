package com.muye.wp.service.impl;

import com.muye.wp.common.cons.RespStatus;
import com.muye.wp.common.exception.WPException;
import com.muye.wp.dao.domain.CommunityModule;
import com.muye.wp.dao.domain.query.CommunityModuleQuery;
import com.muye.wp.dao.mapper.CommunityModuleMapper;
import com.muye.wp.dao.page.Page;
import com.muye.wp.service.CommunityModuleService;
import com.muye.wp.service.CommunityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Created by muye on 18/3/1.
 */
@Service
public class CommunityModuleServiceImpl implements CommunityModuleService {

    @Autowired
    private CommunityModuleMapper communityModuleMapper;

    @Autowired
    private CommunityService communityService;

    @Override
    public CommunityModule queryById(Long id) {
        return communityModuleMapper.selectById(id);
    }

    @Override
    public List<CommunityModule> queryByCondition(CommunityModuleQuery query, Page page) {
        return communityModuleMapper.selectByCondition(query, page);
    }

    @Override
    public void add(CommunityModule communityModule) {
        if (communityService.queryById(communityModule.getCommunityId()) == null){
            throw new WPException(RespStatus.RESOURCE_NOT_EXIST);
        }

        CommunityModuleQuery query = new CommunityModuleQuery();
        query.setCommunityId(communityModule.getCommunityId());
        query.setModuleName(communityModule.getModuleName());
        if (queryByCondition(query, null).size() > 0){
            throw new WPException(RespStatus.RESOURCE_EXISTED);
        }

        communityModuleMapper.insert(communityModule);
    }

    @Override
    @Transactional
    public void update(CommunityModule communityModule) {
        communityModuleMapper.selectByIdForUpdate(communityModule.getId());
        communityModuleMapper.update(communityModule);
    }

    @Override
    public void delete(Long id) {
        communityModuleMapper.delete(id);
    }
}
