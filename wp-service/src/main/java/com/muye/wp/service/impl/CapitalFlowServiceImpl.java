package com.muye.wp.service.impl;

import com.muye.wp.common.cons.CapitalFlowDirection;
import com.muye.wp.common.cons.CapitalFlowStatus;
import com.muye.wp.common.cons.ProductType;
import com.muye.wp.dao.domain.CapitalFlow;
import com.muye.wp.dao.mapper.CapitalFlowMapper;
import com.muye.wp.dao.page.Page;
import com.muye.wp.service.CapitalFlowService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

/**
 * Created by muye on 18/3/30.
 */
@Service
public class CapitalFlowServiceImpl implements CapitalFlowService {

    @Autowired(required = false)
    private CapitalFlowMapper capitalFlowMapper;

    @Override
    public List<CapitalFlow> queryListByCondition(CapitalFlow query, Page page) {
        return capitalFlowMapper.selectListByCondition(query, page);
    }

    @Override
    public void add(CapitalFlow flow) {
        capitalFlowMapper.insert(flow);
    }

    @Override
    public void add(Long userId, CapitalFlowDirection direction, ProductType type, String orderNum, BigDecimal amount) {
        CapitalFlow capitalFlow = new CapitalFlow();
        capitalFlow.setUserId(userId);
        capitalFlow.setDirection(direction.getDirection());
        capitalFlow.setType(type.getType());
        capitalFlow.setOrderNum(orderNum);
        capitalFlow.setAmount(amount);
        capitalFlow.setStatus(CapitalFlowStatus.ING.getStatus());
        add(capitalFlow);
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
