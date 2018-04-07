package com.muye.wp.service.impl;

import com.muye.wp.common.cons.RespStatus;
import com.muye.wp.common.exception.WPException;
import com.muye.wp.dao.domain.Community;
import com.muye.wp.dao.mapper.CommunityMapper;
import com.muye.wp.dao.page.Page;
import com.muye.wp.service.CommunityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Created by muye on 18/2/28.
 */
@Service
public class CommunityServiceImpl implements CommunityService {

    @Autowired(required = false)
    private CommunityMapper communityMapper;

    @Override
    public Community queryById(Long id) {
        return communityMapper.selectById(id);
    }

    @Override
    public List<Community> queryByCondition(Community query, Page page) {
        return communityMapper.selectByCondition(query, page);
    }

    @Override
    public void add(Community community) {
        Community query = new Community();
        query.setCommunityName(community.getCommunityName());
        query.setProvince(community.getProvince());
        query.setCity(community.getCity());
        query.setArea(community.getArea());
        if (queryByCondition(query, null).size() > 0){
            throw new WPException(RespStatus.RESOURCE_EXISTED);
        }
        communityMapper.insert(community);
    }

    @Override
    @Transactional
    public void update(Community community) {
        communityMapper.selectByIdForUpdate(community.getId());
        communityMapper.update(community);
    }

    @Override
    public void delete(Long id) {
        communityMapper.delete(id);
    }
}
