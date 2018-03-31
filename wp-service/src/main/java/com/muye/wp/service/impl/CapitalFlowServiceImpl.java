package com.muye.wp.service.impl;

import com.muye.wp.dao.domain.CapitalFlow;
import com.muye.wp.dao.mapper.CapitalFlowMapper;
import com.muye.wp.service.CapitalFlowService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Created by muye on 18/3/30.
 */
@Service
public class CapitalFlowServiceImpl implements CapitalFlowService {

    @Autowired(required = false)
    private CapitalFlowMapper capitalFlowMapper;

    @Override
    public void add(CapitalFlow flow) {
        capitalFlowMapper.insert(flow);
    }

    @Override
    public CapitalFlow queryByOrderNum(String orderNum) {
        return capitalFlowMapper.selectByOrderNum(orderNum);
    }

    @Override
    public void update(CapitalFlow flow) {
        capitalFlowMapper.update(flow);
    }
}
